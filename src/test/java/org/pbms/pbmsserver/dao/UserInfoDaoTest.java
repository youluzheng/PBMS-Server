package org.pbms.pbmsserver.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserInfoDaoTest {
    private static Logger log = LoggerFactory.getLogger(UserInfoDaoTest.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @BeforeEach
    void setUp() {
        this.userInfoDao.delete(c -> c);
    }

    @Test
    void testInsert_generateKey() {
        UserInfo user = new UserInfo();
        user.setUserName("张三");
        user.setPassword(EncryptUtil.sha512("123456"));
        user.setEmail("zyl@965.life");
        user.setCreateTime(new Date());
        user.setStatus(UserStatusEnum.WAIT_FOR_AUDIT.getCode());

        this.userInfoDao.insert(user);
        log.debug("user:{}", user);
        assertNotNull(user.getUserId());
    }
}