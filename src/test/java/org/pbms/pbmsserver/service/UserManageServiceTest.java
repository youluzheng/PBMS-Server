package org.pbms.pbmsserver.service;

import cn.hutool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.dao.SystemDao;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.common.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserManageServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserManageServiceTest.class);

    @SpyBean
    UserManageService userManageService;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    UserSettingsDao userSettingsDao;

    @MockBean
    SystemDao systemDao;

    @MockBean
    MailService mailService;

    @BeforeEach
    void setUp() {
        // 取消创建文件夹
        doNothing().when(this.systemDao).initAllRespectiveDir(any());
        this.userInfoDao.delete(c -> c);
        this.userSettingsDao.delete(c -> c);
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
    void auditUser_transaction_rollback() {
        doThrow(new RuntimeException()).when(this.userManageService).initDefaultSettings(anyLong());
        doNothing().when(this.mailService).sendMail(anyString(), anyString(), anyString(), any(Context.class));
        UserInfo user = this.insertUser();

        long userId = user.getUserId();
        assertThrows(RuntimeException.class, () -> this.userManageService.auditUser(userId, true));

        UserInfo userInfo = this.userInfoDao.selectByPrimaryKey(user.getUserId())
                .orElseThrow(RuntimeException::new);
        assertEquals(user.getUserName(), userInfo.getUserName());
        assertEquals(UserStatusEnum.WAIT_FOR_AUDIT.getCode(), userInfo.getStatus());

        assertTrue(this.userSettingsDao.selectByPrimaryKey(user.getUserId()).isEmpty());
    }

    @Test
    void auditUser_pass_transaction_commit() {
        UserInfo user = this.insertUser();

        this.userManageService.auditUser(user.getUserId(), true);

        UserInfo userInfo = this.userInfoDao.selectByPrimaryKey(user.getUserId())
                .orElseThrow(RuntimeException::new);
        assertEquals(user.getUserName(), userInfo.getUserName());
        assertEquals(UserStatusEnum.NORMAL.getCode(), userInfo.getStatus());

        UserSettings userSettings = this.userSettingsDao.selectByPrimaryKey(user.getUserId())
                .orElseThrow(RuntimeException::new);
        assertFalse(userSettings.getWatermarkLogoEnable());
    }

    @Test
    void auditUser_fail_transaction_commit() {
        UserInfo user = this.insertUser();
        this.userManageService.auditUser(user.getUserId(), false);

        UserInfo userInfo = this.userInfoDao.selectByPrimaryKey(user.getUserId())
                .orElseThrow(RuntimeException::new);
        assertEquals(user.getUserName(), userInfo.getUserName());
        assertEquals(UserStatusEnum.AUDIT_FAIL.getCode(), userInfo.getStatus());

        assertTrue(this.userSettingsDao.selectByPrimaryKey(user.getUserId()).isEmpty());
    }

    @Test
    public void deleteUser_test() {
        UserInfo userInfo = this.insertUser();
        this.userManageService.deleteUser(userInfo.getUserId());
        assertThrows(NoSuchElementException.class, () -> userInfoDao.selectByPrimaryKey(userInfo.getUserId()).get());
    }
}