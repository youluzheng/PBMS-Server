package org.pbms.pbmsserver.controller;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.request.user.UserRegisterReq;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.mapper.UserSettingsDynamicSqlSupport;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.service.SystemService;
import org.pbms.pbmsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SystemControllerTest extends BaseControllerTestWithAuth {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSettingsDao userSettingsDao;
    @Autowired
    private SystemService systemManageService;
    private UserRegisterReq req;
    private final JsonMapper jsonMapper = new JsonMapper();

    @Override
    protected void setup() {
        this.req = new UserRegisterReq();
        req.setUserName("王大锤");
        req.setPassword("123456");
        req.setEmail("123@123.com");
    }

    @Test
    public void checkInit_false_test() throws Exception {
        userInfoDao.delete(c -> c);
        userSettingsDao.delete(c -> c);
        get("/system")
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void checkInit_true_test() throws Exception {
        get("/system")
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private static Stream<Arguments> systemAdmin_invalid() {
        return Stream.of(
                Arguments.of(new UserRegisterReq("", "123456", "123@email")),
                Arguments.of(new UserRegisterReq(null, "123456", "123@email")),
                Arguments.of(new UserRegisterReq("wqe", "1234", "123@email")),
                Arguments.of(new UserRegisterReq("wqe", "123456789012345678901234567890", "123@email")),
                Arguments.of(new UserRegisterReq("wqe", null, "123@email")),
                Arguments.of(new UserRegisterReq("wqe", "null123", "null")),
                Arguments.of(new UserRegisterReq("wqe", "null123", "")),
                Arguments.of(new UserRegisterReq("wqe", "null123", null))
        );
    }

    @ParameterizedTest
    @MethodSource("systemAdmin_invalid")
    public void systemAdmin_invalid(UserRegisterReq req) throws Exception {
        userInfoDao.delete(c -> c);
        userSettingsDao.delete(c -> c);
        post("/system/admin", req)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void systemAdmin_has_admin() throws Exception {
        post("/system/admin", req)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void systemAdmin_normal() throws Exception {
        userInfoDao.delete(c -> c);
        userSettingsDao.delete(c -> c);
        post("/system/admin", req)
                .andExpect(status().isOk());
        assertDoesNotThrow(() -> userInfoDao.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("王大锤"))).get());
        UserInfo userInfo = userInfoDao.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("王大锤"))).get();
        assertFalse(userSettingsDao.selectOne(c -> c.where(UserSettingsDynamicSqlSupport.userId, isEqualTo(userInfo.getUserId()))).isEmpty());
        assertTrue(FileUtil.exist(ServerConstant.getAbsoluteTempPath(userInfo)));
        assertTrue(FileUtil.exist(ServerConstant.getAbsoluteUploadPath(userInfo)));
        assertTrue(FileUtil.exist(ServerConstant.getAbsoluteLogoPath(userInfo)));
    }
}
