package org.pbms.pbmsserver.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.common.request.user.UserRegisterDTO;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport.userName;

@SpringBootTest
public class SystemManageServiceTest {

    @SpyBean
    SystemService systemManageService;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    UserInfoMapper userInfoMapper;
    @MockBean
    UserManageService userManageService;

    private UserRegisterDTO req = new UserRegisterDTO("王大锤", "123456", "fff@22.cn");

    @BeforeEach
    public void beforeEach() {
        this.userInfoMapper.delete(c -> c);
    }

    @Test
    public void systemManageService_transaction_rollback() {
        doThrow(new RuntimeException()).when(this.userManageService).initDefaultSettings(anyLong());
        assertThrows(RuntimeException.class, () -> this.systemManageService.initAdmin(req));
        assertThrows(NoSuchElementException.class, () -> this.userInfoDao.selectOne(c -> c.where(userName, isEqualTo("王大锤"))).get());
    }
}
