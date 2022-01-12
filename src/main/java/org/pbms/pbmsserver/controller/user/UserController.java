package org.pbms.pbmsserver.controller.user;

import org.hibernate.validator.constraints.Length;
import org.pbms.pbmsserver.common.auth.Role;
import org.pbms.pbmsserver.common.auth.RoleEnum;
import org.pbms.pbmsserver.common.auth.TokenHandler;
import org.pbms.pbmsserver.common.request.user.PasswordModifyDTO;
import org.pbms.pbmsserver.common.request.user.SettingModifyDTO;
import org.pbms.pbmsserver.common.request.user.UserLoginDTO;
import org.pbms.pbmsserver.common.request.user.UserRegisterDTO;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 王俊
 * @author zyl
 * @since 0.3.0
 */
@RestController
@RequestMapping("user")
@Validated
@Role(role = RoleEnum.ALL_LOGGED_IN)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserSettingsDao userSettingsDao;

    @PostMapping("action-login")
    @Role
    public String login(@Validated @RequestBody UserLoginDTO req) {
        return this.userService.login(req.getUserName(), req.getPassword());
    }

    @PostMapping
    @Role
    public void register(@RequestBody @Validated UserRegisterDTO req) {
        userService.register(req);
    }

    @GetMapping("registerLink/action-check")
    @Role
    public void checkRegisterLink(@RequestParam @NotNull Long userId, @RequestParam @NotBlank @Length(min = 32, max = 32) String code) {
        log.debug("userId:{}, code:{}", userId, code);
        userService.checkRegisterLink(userId, code);
    }

    /**
     * 忘记密码发送邮件
     *
     * @param email    邮箱
     * @param userName 用户名
     */
    @GetMapping("emailCaptcha")
    @Role
    public void getEmailCaptcha(@RequestParam @Email String email, @RequestParam @NotEmpty String userName) {
        userService.getChangePasswordEmailCaptcha(email, userName);
    }

    /**
     * 用户点击邮件内忘记密码链接，跳转至修改密码页面
     *
     * @param userId 用户id
     * @param code   验证code
     * @return 用户token
     */
    @GetMapping("passwordLink/action-check")
    @Role
    public String checkChangeCode(@RequestParam @NotNull Long userId, @RequestParam @NotBlank @Length(min = 32, max = 32) String code) {
        return userService.checkChangePasswordMail(userId, code);
    }

    @PutMapping("password")
    public void changePassword(@RequestBody @Validated PasswordModifyDTO req) {
        userService.changePassword(req.getPassword());
    }

    @PutMapping("settings")
    public void modifyUserSettings(@RequestBody @Validated SettingModifyDTO req) {
        UserSettings userSettings = req.transform();
        userSettings.setUserId(TokenHandler.getUserId());
        this.userSettingsDao.updateByPrimaryKeySelective(userSettings);
    }

    @GetMapping("settings")
    public UserSettings getUserSettings() {
        return this.userService.getSettings();
    }
}
