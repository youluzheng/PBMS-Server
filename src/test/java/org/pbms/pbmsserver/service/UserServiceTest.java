package org.pbms.pbmsserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @SpyBean
    UserService userService;

    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    UserSettingsDao userSettingsDao;

    @BeforeEach
    void setUp() {
        this.userInfoDao.delete(c -> c);
        this.userSettingsDao.delete(c -> c);
    }

    private UserInfo insertUser() {
        UserInfo user = new UserInfo();
        user.setUserName("张三");
        user.setPassword(EncryptUtil.sha512("123456"));
        user.setEmail("zyl@965.life");
        user.setCreateTime(new Date());
        user.setStatus(UserStatusEnum.WAIT_FOR_AUDIT.getCode());
        this.userInfoDao.insert(user);
        return user;
    }

    @Test
    void auditUser_transaction_rollback() {
        doThrow(new RuntimeException()).when(this.userService).initDefaultSettings(anyLong());

        UserInfo user = this.insertUser();

        long userId = user.getUserId();
        assertThrows(RuntimeException.class, () -> this.userService.auditUser(userId, true));

        UserInfo userInfo = this.userInfoDao.selectByPrimaryKey(user.getUserId())
                .orElseThrow(RuntimeException::new);
        assertEquals(user.getUserName(), userInfo.getUserName());
        assertEquals(UserStatusEnum.WAIT_FOR_AUDIT.getCode(), userInfo.getStatus());

        assertTrue(this.userSettingsDao.selectByPrimaryKey(user.getUserId()).isEmpty());
    }

    @Test
    void auditUser_pass_transaction_commit() {
        UserInfo user = this.insertUser();

        this.userService.auditUser(user.getUserId(), true);

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

        this.userService.auditUser(user.getUserId(), false);

        UserInfo userInfo = this.userInfoDao.selectByPrimaryKey(user.getUserId())
                .orElseThrow(RuntimeException::new);
        assertEquals(user.getUserName(), userInfo.getUserName());
        assertEquals(UserStatusEnum.AUDIT_FAIL.getCode(), userInfo.getStatus());

        assertTrue(this.userSettingsDao.selectByPrimaryKey(user.getUserId()).isEmpty());
    }
}