package org.pbms.pbmsserver.service;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@SpringBootTest
public class UserServiceTest {

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
        ReflectionTestUtils.setField(userService, "codeMap", map);
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
        try {
            userService.register(req);
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.USERNAME_ALREADY_EXISTS.toString());
        }
    }

    @Test
    public void register_repeat_email_test() {
        insertUser();
        req.setEmail("zyl@965.life");
        try {
            userService.register(req);
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.EMAIL_REGISTERED.toString());
        }
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
        try {
            when(map.get(anyLong())).thenReturn("1111");
            userService.checkRegisterLink(user.getUserId(), "");
        } catch (ClientException e) {
            assertEquals(e.getMessage(), "操作失败");
        }
    }

    @Test
    public void checkRegisterLink_null_test() {
        UserInfo user = insertUser();
        try {
            userService.checkRegisterLink(user.getUserId(), "");
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.INVALID_LINK.toString());
        }
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
        try {
            userService.getChangePasswordEmailCaptcha("1254@34.com", user.getUserName());
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.USER_NOT_FOUND.toString());
        }

        try {
            userService.getChangePasswordEmailCaptcha(user.getEmail(), "user.getUserName()");
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.USER_NOT_FOUND.toString());
        }
        userInfoDao.delete(c -> c);
        insertUser();
        try {
            userService.getChangePasswordEmailCaptcha(user.getEmail(), user.getUserName());
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.USER_NOT_FOUND.toString());
        }
    }

    @Test
    public void checkChangePasswordMail_normal_test() {
        UserInfo user = insertUser();
        doReturn(user).when(userService).checkCode(anyLong(), anyString());
        assertDoesNotThrow(() -> userService.checkChangePasswordMail(user.getUserId(), ""));
    }

    @Test
    public void checkChangePasswordMail_empty_param_test() {
        UserInfo user = insertUser();
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfoDao.delete(c -> c);
        userInfoDao.insert(user);
        userService.getChangePasswordEmailCaptcha(user.getEmail(), user.getUserName());
        try {
            userService.checkChangePasswordMail(user.getUserId(), "12345678123456781234567812345678");
            fail("未捕获指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.INVALID_LINK.toString());
        }
    }

    @Test
    public void checkRegisterLink_wrong_code_test() {
        userService.register(req);
        UserInfo userInfo = userInfoDao.selectOne(c -> c
                .where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo(req.getUserName()))
        ).orElseThrow(() -> new ClientException(""));
        try {
            when(map.get(anyLong())).thenReturn("1111");
            userService.checkRegisterLink(userInfo.getUserId(), "");
        } catch (ClientException e) {
            assertEquals(e.getMessage(), "无效链接");
        }
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
