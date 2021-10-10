package org.pbms.pbmsserver.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.request.user.SettingModifyReq;
import org.pbms.pbmsserver.common.request.user.UserLoginReq;
import org.pbms.pbmsserver.common.request.user.UserRegisterReq;
import org.pbms.pbmsserver.dao.UserInfoDao;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.common.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseControllerTestWithAuth {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserSettingsDao userSettingsDao;
    @MockBean
    private MailService mailService;
    private final JsonMapper jsonMapper = new JsonMapper();
    private UserLoginReq req = new UserLoginReq();

    @Override
    protected void setup() {
        userInfoDao.delete(c -> c);
        userSettingsDao.delete(c -> c);
        // 取消发送邮件
        doNothing().when(mailService).sendMail(anyString(), anyString(), anyString(), any(Context.class));
    }

    @Test
    public void login_normal_test() throws Exception {
        this.req.setUserName(userInfo.getUserName());
        this.req.setPassword("123456");
        post("/user/action-login", req)
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> login_invalid() {
        return Stream.of(
                Arguments.of(new UserLoginReq("", "123456")),
                Arguments.of(new UserLoginReq(null, "123456")),
                Arguments.of(new UserLoginReq("admin", "1234567")),
                Arguments.of(new UserLoginReq("admin", "")),
                Arguments.of(new UserLoginReq("admin", null))
        );
    }

    @ParameterizedTest
    @MethodSource("login_invalid")
    public void login_invalid(UserLoginReq req) throws Exception {
        post("/user/action-login", req)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void register_normal_test() throws Exception {
        doNothing().when(mailService).sendMail(anyString(), anyString(), anyString(), any(Context.class));
        UserRegisterReq req = new UserRegisterReq();
        req.setUserName("张三a");
        req.setPassword("123123");
        req.setEmail("123@123.com");
        post("/user", req)
                .andExpect(status().isOk());
        assertDoesNotThrow(() -> userInfoDao.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("张三a"))).get());
        assertEquals(UserStatusEnum.UNCHECKED.getCode(), userInfoDao.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("张三a"))).get().getStatus());
    }


    @Test
    public void register_name_repeat_test() throws Exception {
        UserRegisterReq req = new UserRegisterReq();
        req.setUserName("admin");
        req.setPassword("123123");
        req.setEmail("123@123.com");
        post("/user", req)
                .andExpect(status().is4xxClientError());
        assertThrows(NoSuchElementException.class, () -> userInfoDao.selectOne(c -> c.where(UserInfoDynamicSqlSupport.userInfo.userName, isEqualTo("张三"))).get());
    }

    @Test
    public void register_repeat_test() throws Exception {
        UserRegisterReq req = new UserRegisterReq();
        req.setUserName("张三啊");
        req.setPassword("123123");
        req.setEmail("1234@1234.com");
        post("/user", req)
                .andExpect(status().isOk());
        post("/user", req)
                .andExpect(status().is4xxClientError());
    }

    private static Stream<Arguments> register_repeat() {
        return Stream.of(
                Arguments.of("王达成", "123@123.com", UserStatusEnum.NORMAL.getCode(), "王达成", "12@12.com"),
                Arguments.of("王达成", "123@123.com", UserStatusEnum.WAIT_FOR_AUDIT.getCode(), "王达成", "12@12.com"),
                Arguments.of("王达成", "123@123.com", UserStatusEnum.FORBID.getCode(), "王达成", "12@12.com"),
                Arguments.of("王小成", "123@123.com", UserStatusEnum.NORMAL.getCode(), "王达成", "123@123.com"),
                Arguments.of("王小成", "123@123.com", UserStatusEnum.WAIT_FOR_AUDIT.getCode(), "王达成", "123@123.com"),
                Arguments.of("王小成", "123@123.com", UserStatusEnum.FORBID.getCode(), "王达成", "123@123.com"),
                Arguments.of("王小成", "123@123.com", UserStatusEnum.AUDIT_FAIL.getCode(), "王达成", "123@123.com")
        );
    }

    @ParameterizedTest
    @MethodSource("register_repeat")
    public void register_state_repeat(String oldName, String oldEmail, byte oldState, String newName, String newEmail) throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(oldName);
        userInfo.setEmail(oldEmail);
        userInfo.setPassword("123456");
        userInfo.setStatus(oldState);
        userInfo.setCreateTime(new Date());
        userInfo.setRole(UserRoleEnum.NORMAL.getCode());
        userInfoDao.insert(userInfo);
        new UserRegisterReq(newName, "123456", newEmail);
        post("/user", req)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void modifyUserSettings_normal_test() throws Exception {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(userInfo.getUserId());
        userSettings.setCompressScale((byte) 80);
        userSettings.setWatermarkTextEnable(false);
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setResponseReturnType("md");
        userSettingsDao.insert(userSettings);
        SettingModifyReq settingModifyReq = new SettingModifyReq();
        settingModifyReq.setCompressScale((byte) 80);
        settingModifyReq.setResponseReturnType("md");
        settingModifyReq.setWatermarkLogoEnable(false);
        settingModifyReq.setWatermarkTextEnable(false);
        put("/user/settings", settingModifyReq, MediaType.APPLICATION_JSON)
                .andExpect(status().isOk());
        assertEquals((byte) 80, userSettingsDao.selectByPrimaryKey(this.userInfo.getUserId()).get().getCompressScale());
        assertEquals("md", userSettingsDao.selectByPrimaryKey(this.userInfo.getUserId()).get().getResponseReturnType());
        assertEquals(false, userSettingsDao.selectByPrimaryKey(this.userInfo.getUserId()).get().getWatermarkLogoEnable());
        assertEquals(false, userSettingsDao.selectByPrimaryKey(this.userInfo.getUserId()).get().getWatermarkTextEnable());
    }


    @Test
    public void getUserSettings_test() throws Exception {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(userInfo.getUserId());
        userSettings.setCompressScale((byte) 80);
        userSettings.setWatermarkTextEnable(false);
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setResponseReturnType("md");
        userSettingsDao.insert(userSettings);
        get("/user/settings")
                .andExpect(status().isOk());
    }
}
