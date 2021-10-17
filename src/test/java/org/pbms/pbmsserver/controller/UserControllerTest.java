package org.pbms.pbmsserver.controller;

import cn.hutool.cache.impl.TimedCache;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.request.user.ChangePasswordReq;
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
import org.pbms.pbmsserver.service.UserService;
import org.pbms.pbmsserver.service.common.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseControllerTestWithAuth {
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private UserSettingsDao userSettingsDao;
    @Autowired
    private UserService userService;
    @MockBean
    private MailService mailService;
    @Mock
    private TimedCache<Long, String> map;
    private final JsonMapper jsonMapper = new JsonMapper();
    private UserLoginReq req = new UserLoginReq();
    private HashMap<String, String> checkRegisterParams;
    private HashMap<String, String> getCaptchaParams;
    private UserInfo another;

    @Override
    protected void setup() {
        userInfoDao.delete(c -> c);
        userSettingsDao.delete(c -> c);
        another = new UserInfo();
        another.setUserName("xxx");
        another.setStatus(UserStatusEnum.UNCHECKED.getCode());
        another.setRole(UserRoleEnum.NORMAL.getCode());
        another.setCreateTime(new Date());
        another.setEmail("123@123.com");
        another.setPassword("xxxxxx");
        userInfoDao.insert(another);
        checkRegisterParams = new HashMap<>() {{
            put("userId", another.getUserId().toString());
            put("code", "12345678123456781234567812345678");
        }};
        getCaptchaParams = new HashMap<>() {{
            put("email", "zyl@965.life");
            put("userName", "admin");
        }};
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
        req.setUserName("admin");
        req.setPassword("123123");
        req.setEmail("1234@1234.com");
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

    @Test
    public void checkRegisterLink_invalid_test() throws Exception {
        get("/user/registerLink/action-check", checkRegisterParams)
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(content().string("{\"code\":01005, \"message\":\"链接已失效\"}"));
    }

    @Test
    public void checkRegisterLink_invalid_wrong_code_test() throws Exception {
        ReflectionTestUtils.setField(userService, "codeMap", map);
        when(map.get(another.getUserId())).thenReturn("12345678123456781234567812345679");
        get("/user/registerLink/action-check", checkRegisterParams)
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("无效链接"));
    }

    @Test
    public void checkRegisterLink_success_test() throws Exception {
        ReflectionTestUtils.setField(userService, "codeMap", map);
        when(map.get(another.getUserId())).thenReturn("12345678123456781234567812345678");
        get("/user/registerLink/action-check", checkRegisterParams)
                .andExpect(status().isOk());
        assertEquals(UserStatusEnum.WAIT_FOR_AUDIT.getCode(), userInfoDao.selectByPrimaryKey(another.getUserId()).orElse(userInfo).getStatus());
    }

    @Test
    public void getEmailCaptcha_success_test() throws Exception {
        get("/user/emailCaptcha", getCaptchaParams)
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> getEmailCaptcha_invalid() {
        return Stream.of(
                Arguments.of("admin1", "zyl@965.life", "用户名错误"),
                Arguments.of("admin", "zyl@965.life1", "邮箱错误"),
                Arguments.of("admin1", "zyl@965.life1", "用户名邮箱错误")

        );
    }

    @ParameterizedTest
    @MethodSource("getEmailCaptcha_invalid")
    public void getEmailCaptcha_wrong_test(String name, String email) throws Exception {
        getCaptchaParams.put("email", email);
        getCaptchaParams.put("userName", name);
        get("/user/emailCaptcha", getCaptchaParams)
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"code\":01002, \"message\":\"用户不存在\"}"));
    }

    @Test
    public void getEmailCaptcha_repeat_test() throws Exception {
        ReflectionTestUtils.setField(userService, "codeMap", map);
        when(map.get(userInfo.getUserId())).thenReturn("12345678123456781234567812345679");
        get("/user/emailCaptcha", getCaptchaParams)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("邮件已发送，稍后再试"));
    }

    @Test
    public void checkChangeCode_invalid_test() throws Exception {
        get("/user/password-page", checkRegisterParams)
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andExpect(content().string("{\"code\":01005, \"message\":\"链接已失效\"}"));
    }

    @Test
    public void checkChangeCode_invalid_wrong_code_test() throws Exception {
        ReflectionTestUtils.setField(userService, "codeMap", map);
        when(map.get(another.getUserId())).thenReturn("12345678123456781234567812345679");
        get("/user/password-page", checkRegisterParams)
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string("无效链接"));
    }

    @Test
    public void checkChangeCode_success_test() throws Exception {
        ReflectionTestUtils.setField(userService, "codeMap", map);
        when(map.get(another.getUserId())).thenReturn("12345678123456781234567812345678");
        get("/user/password-page", checkRegisterParams)
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword_test() throws Exception {
        ChangePasswordReq req = new ChangePasswordReq("12123123");
        put("/user/password", req)
                .andExpect(status().isOk());
        UserInfo userInfo = userInfoDao.selectByPrimaryKey(this.userInfo.getUserId()).orElse(this.userInfo);
        assertEquals(userInfo.getPassword(), ServerConstant.HASH_METHOD.apply(req.getPassword()));
    }
}
