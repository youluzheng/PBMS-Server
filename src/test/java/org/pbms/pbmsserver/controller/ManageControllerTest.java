package org.pbms.pbmsserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.dao.SystemDao;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserManageService;
import org.pbms.pbmsserver.service.common.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManageControllerTest extends BaseControllerTest {
    @Autowired
    private UserManageService userManageService;
    @MockBean
    private MailService mailService;
    @Autowired
    private UserSettingsMapper userSettingsMapper;
    @MockBean
    private SystemDao systemDao;
    @Autowired
    private UserInfoMapper userInfoMapper;

    private UserInfo admin;

    private UserInfo user;

    @Override
    protected TokenBean getTokenBean() {
        return new TokenBean(this.admin.getUserId(), this.admin.getUserName(), this.admin.getRole());
    }

    @BeforeEach
    void setup() {
        userInfoMapper.delete(c -> c);
        this.admin = this.insertDefaultAdmin();

        user = new UserInfo();
        user.setUserName("ffff");
        user.setPassword("12323");
        user.setEmail("123@123.com");
        user.setCreateTime(new Date());
        user.setRole(UserRoleEnum.NORMAL.getCode());
        user.setStatus(UserStatusEnum.NORMAL.getCode());
        userInfoMapper.insert(user);
    }

    private static Stream<Arguments> forbiddenUser_invalid() {
        return Stream.of(
                Arguments.of(UserStatusEnum.WAIT_FOR_AUDIT.getCode()),
                Arguments.of(UserStatusEnum.UNCHECKED.getCode()),
                Arguments.of(UserStatusEnum.AUDIT_FAIL.getCode()),
                Arguments.of(UserStatusEnum.FORBID.getCode())
        );
    }

    @Test
    public void forbiddenUser_normal_test() throws Exception {
        patch("/user/" + user.getUserId() + "/action-forbidden")
                .andExpect(status().isOk());
        assertEquals(UserStatusEnum.FORBID.getCode(), userInfoMapper.selectByPrimaryKey(user.getUserId()).get().getStatus());
    }

    @ParameterizedTest()
    @MethodSource("forbiddenUser_invalid")
    public void forbiddenUser_status_illegal_test(byte status) throws Exception {
        user.setStatus(status);
        userInfoMapper.updateByPrimaryKey(user);
        patch("/user/" + user.getUserId() + "/action-forbidden")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void unset_normal_test() throws Exception {
        user.setStatus(UserStatusEnum.FORBID.getCode());
        userInfoMapper.updateByPrimaryKey(user);
        patch("/user/" + user.getUserId() + "/action-unset")
                .andExpect(status().isOk());
        assertEquals(UserStatusEnum.NORMAL.getCode(), userInfoMapper.selectByPrimaryKey(user.getUserId()).get().getStatus());
    }

    private static Stream<Arguments> unsetUser_invalid() {
        return Stream.of(
                Arguments.of(UserStatusEnum.UNCHECKED.getCode())
        );
    }

    @ParameterizedTest
    @MethodSource("unsetUser_invalid")
    public void unsetUser_status_illegal_test(byte status) throws Exception {
        user.setStatus(status);
        userInfoMapper.updateByPrimaryKey(user);
        patch("/user/" + user.getUserId() + "/action-unset")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void auditUser_normal_pass_test() throws Exception {
        user.setStatus(UserStatusEnum.WAIT_FOR_AUDIT.getCode());
        userInfoMapper.updateByPrimaryKey(user);
        HashMap<String, String> params = new HashMap<>();
        params.put("pass", "true");
        patch("/user/" + user.getUserId() + "/action-audit", params)
                .andExpect(status().isOk());
        assertEquals(UserStatusEnum.NORMAL.getCode(), userInfoMapper.selectByPrimaryKey(user.getUserId()).get().getStatus());
    }

    @Test
    public void auditUser_normal_reject_test() throws Exception {
        user.setStatus(UserStatusEnum.WAIT_FOR_AUDIT.getCode());
        userInfoMapper.updateByPrimaryKey(user);
        Map<String, String> params = new HashMap<>();
        params.put("pass", "false");

        patch("/user/" + user.getUserId() + "/action-audit", params)
                .andExpect(status().isOk());
        assertThrows(NoSuchElementException.class, () -> userSettingsMapper.selectByPrimaryKey(user.getUserId()).get(), "No value present");
        assertEquals(UserStatusEnum.AUDIT_FAIL.getCode(), userInfoMapper.selectByPrimaryKey(user.getUserId()).get().getStatus());
    }

    @Test
    public void auditUser_illegal_userId_test() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("pass", "false");
        patch("/user/123/action-audit", params)
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteUser_normal_test() throws Exception {
        doNothing().when(systemDao).removeAllRespectiveDir(anyLong());

        delete("/user/" + user.getUserId())
                .andExpect(status().isOk());
        assertThrows(NoSuchElementException.class, () -> userSettingsMapper.selectByPrimaryKey(user.getUserId()).get(), "No value present");
        assertThrows(NoSuchElementException.class, () -> userInfoMapper.selectByPrimaryKey(user.getUserId()).get(), "No value present");
    }

    @Test
    public void deleteUser_illegal_userId_test() throws Exception {
        doNothing().when(systemDao).removeAllRespectiveDir(anyLong());

        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(user.getUserId());
        userSettings.setCompressScale((byte) 80);
        userSettings.setWatermarkLogoEnable(true);
        userSettings.setWatermarkTextEnable(true);
        userSettings.setResponseReturnType("md");
        userSettingsMapper.insert(userSettings);
        delete("/user/12343544")
                .andExpect(status().isBadRequest());
        assertDoesNotThrow(() -> userSettingsMapper.selectByPrimaryKey(user.getUserId()).get());
        assertDoesNotThrow(() -> userInfoMapper.selectByPrimaryKey(user.getUserId()).get());
    }

}
