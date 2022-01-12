package org.pbms.pbmsserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.request.user.UserRegisterDTO;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.mapper.UserSettingsDynamicSqlSupport;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.service.SystemService;
import org.pbms.pbmsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SystemControllerTest extends BaseControllerTest {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSettingsMapper userSettingsMapper;
    @Autowired
    private SystemService systemManageService;
    private UserRegisterDTO req;

    private UserInfo admin;

    @Override
    protected TokenBean getTokenBean() {
        return new TokenBean(this.admin.getUserId(), this.admin.getUserName(), UserRoleEnum.transform(this.admin.getRole()));
    }

    @BeforeEach
    void setup() {
        userInfoMapper.delete(c -> c);
        userSettingsMapper.delete(c -> c);
        this.admin = this.insertDefaultAdmin();

        this.req = new UserRegisterDTO();
        req.setUserName("王大锤");
        req.setPassword("123456");
        req.setEmail("123@123.com");
    }

    @Test
    public void checkInit_false_test() throws Exception {
        userInfoMapper.delete(c -> c);
        userSettingsMapper.delete(c -> c);
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
                Arguments.of(new UserRegisterDTO("", "123456", "123@email")),
                Arguments.of(new UserRegisterDTO(null, "123456", "123@email")),
                Arguments.of(new UserRegisterDTO("wqe", "1234", "123@email")),
                Arguments.of(new UserRegisterDTO("wqe", "123456789012345678901234567890", "123@email")),
                Arguments.of(new UserRegisterDTO("wqe", null, "123@email")),
                Arguments.of(new UserRegisterDTO("wqe", "null123", "null")),
                Arguments.of(new UserRegisterDTO("wqe", "null123", "")),
                Arguments.of(new UserRegisterDTO("wqe", "null123", null))
        );
    }

    @ParameterizedTest
    @MethodSource("systemAdmin_invalid")
    public void systemAdmin_invalid(UserRegisterDTO req) throws Exception {
        userInfoMapper.delete(c -> c);
        userSettingsMapper.delete(c -> c);
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
        userInfoMapper.delete(c -> c);
        userSettingsMapper.delete(c -> c);
        post("/system/admin", req)
                .andExpect(status().isOk());
        assertDoesNotThrow(() -> userInfoMapper.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("王大锤"))).get());
        UserInfo userInfo = userInfoMapper.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("王大锤"))).get();
        assertFalse(userSettingsMapper.selectOne(c -> c.where(UserSettingsDynamicSqlSupport.userId, isEqualTo(userInfo.getUserId()))).isEmpty());
    }
}
