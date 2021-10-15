package org.pbms.pbmsserver.service;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.ClientException;
import org.pbms.pbmsserver.common.request.user.UserRegisterReq;
import org.pbms.pbmsserver.dao.SystemDao;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.service.common.MailService;
import org.pbms.pbmsserver.util.TokenUtil;
import org.powermock.api.support.membermodification.MemberModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.thymeleaf.context.Context;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@SpringBootTest
public class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @SpyBean
    UserService userService;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    UserSettingsDao userSettingsDao;
    @MockBean
    SystemDao systemDao;
    @MockBean
    MailService mailService;
    @MockBean
    TimedCache<Long, String> map;
    private UserRegisterReq req;


    @BeforeEach
    void setUp() throws IllegalAccessException {
        // 取消创建文件夹
        doNothing().when(this.systemDao).initAllRespectiveDir(any());
        // 取消发送邮件
        doNothing().when(mailService).sendMail(anyString(), anyString(), anyString(), any(Context.class));
        // 清库
        this.userInfoDao.delete(c -> c);
        this.userSettingsDao.delete(c -> c);
        // mock成员变量
        MemberModifier.field(UserService.class, "codeMap")
                .set(userService, map);
        // 初始化req
        req = new UserRegisterReq();
        req.setUserName("Thanos");
        req.setEmail("123@nb.com");
        req.setPassword("123456");
    }

    private UserInfo insertUser() {
        UserInfo user = new UserInfo();
        user.setUserName("张三");
        user.setPassword(DigestUtil.sha256Hex("123456"));
        user.setEmail("zyl@965.life");
        user.setCreateTime(new Date());
        user.setRole(UserRoleEnum.NORMAL.getCode());
        user.setStatus(UserStatusEnum.WAIT_FOR_AUDIT.getCode());
        this.userInfoDao.insert(user);
        return user;
    }

    @Test
    public void register_normal_test() {
        userService.register(req);
        assertEquals(UserStatusEnum.UNCHECKED.getCode(), userInfoDao.selectOne(c -> c.
                where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("Thanos"))).get().getStatus());
    }

    @Test
    public void register_repeat_userName_test() {
        insertUser();
        req.setUserName("张三");
        assertThrows(BusinessException.class, () -> userService.register(req), "用户名称重复");
    }

    @Test
    public void register_repeat_email_test() {
        insertUser();
        req.setEmail("zyl@965.life");
        assertThrows(BusinessException.class, () -> userService.register(req), "邮箱已注册");
    }

    @Test
    public void checkRegisterLink_normal_test() {
        UserInfo user = insertUser();
        user.setStatus(UserStatusEnum.UNCHECKED.getCode());
        doReturn(user).when(userService).checkCode(anyLong(), anyString());
        userService.checkRegisterLink(user.getUserId(), "");
        assertEquals(UserStatusEnum.WAIT_FOR_AUDIT.getCode(), userInfoDao.selectByPrimaryKey(user.getUserId()).get().getStatus());
    }

    @Test
    public void checkRegisterLink_status_illegal_test() {
        UserInfo user = insertUser();
        doReturn(user).when(userService).checkCode(anyLong(), anyString());
        assertThrows(ClientException.class, () -> userService.checkRegisterLink(user.getUserId(), ""), "操作失败");
    }

    @Test
    public void checkRegisterLink_null_test() {
        UserInfo user = insertUser();
        assertThrows(BusinessException.class, () -> userService.checkRegisterLink(user.getUserId(), ""), "链接已失效");
    }

    @Test
    public void getEmailCaptcha_normal_test() {
        UserInfo user = insertUser();
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfoDao.delete(c -> c);
        userInfoDao.insert(user);
        assertDoesNotThrow(() -> userService.getChangePasswordEmailCaptcha(user.getEmail(), user.getUserName()));
    }

    @Test
    public void getEmailCaptcha_wrong_user_test() {
        UserInfo user = insertUser();
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfoDao.delete(c -> c);
        userInfoDao.insert(user);
        assertThrows(BusinessException.class, () -> userService.getChangePasswordEmailCaptcha("1254@34.com", user.getUserName()), "用户不存在");
        assertThrows(BusinessException.class, () -> userService.getChangePasswordEmailCaptcha(user.getEmail(), "user.getUserName()"), "用户不存在");
        userInfoDao.delete(c -> c);
        insertUser();
        assertThrows(BusinessException.class, () -> userService.getChangePasswordEmailCaptcha(user.getEmail(), user.getUserName()), "用户不存在");
    }

    @Test
    public void checkChangePasswordMail_normal_test() {
        UserInfo user = insertUser();
        doReturn(user).when(userService).checkCode(anyLong(), anyString());
        assertDoesNotThrow(() -> userService.checkChangePasswordMail(user.getUserId(), ""));
    }

    @Test
    public void checkChangePasswordMail_wrong_param_test() {
        UserInfo user = insertUser();
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfoDao.delete(c -> c);
        userInfoDao.insert(user);
        userService.getChangePasswordEmailCaptcha(user.getEmail(), user.getUserName());

        assertThrows(BusinessException.class, () -> userService.checkChangePasswordMail(2L, "123"));
        assertThrows(ClientException.class, () -> userService.checkChangePasswordMail(user.getUserId(), "123"));
    }

    @Test
    public void checkRegisterLink_wrong_code_test() {
        userService.register(req);
        UserInfo userInfo = userInfoDao.selectOne(c -> c
                .where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo(req.getUserName()))
        ).orElseThrow(() -> new ClientException(""));
        assertThrows(ClientException.class, () -> userService.checkRegisterLink(userInfo.getUserId(), ""), "无效链接");
    }

    @Test
    public void chPassword_test() {
        UserInfo userInfo = this.insertUser();
        try (MockedStatic<TokenUtil> tokenUtilMockedStatic = mockStatic(TokenUtil.class)) {
            tokenUtilMockedStatic.when(TokenUtil::getUserId).thenReturn(userInfo.getUserId());
            this.userService.changePassword("123123");
            assertEquals(ServerConstant.HASH_METHOD.apply("123123"), userInfoDao.selectByPrimaryKey(userInfo.getUserId()).get().getPassword());
        }
    }
}
