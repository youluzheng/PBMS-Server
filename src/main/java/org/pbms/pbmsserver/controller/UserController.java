package org.pbms.pbmsserver.controller;

import org.hibernate.validator.constraints.Length;
import org.pbms.pbmsserver.common.auth.PublicInterface;
import org.pbms.pbmsserver.common.request.user.ChangePasswordReq;
import org.pbms.pbmsserver.common.request.user.SettingModifyReq;
import org.pbms.pbmsserver.common.request.user.UserLoginReq;
import org.pbms.pbmsserver.common.request.user.UserRegisterReq;
import org.pbms.pbmsserver.dao.UserSettingsDao;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.pbms.pbmsserver.util.TokenUtil;
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
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserSettingsDao userSettingsDao;

    @PostMapping("action-login")
    @PublicInterface
    public String login(@Validated @RequestBody UserLoginReq req) {
        return this.userService.login(req.getUserName(), req.getPassword());
    }

    @PostMapping
    @PublicInterface
    public void register(@RequestBody @Validated UserRegisterReq req) {
        userService.register(req);
    }

    @GetMapping("registerLink/action-check")
    @PublicInterface
    public void checkRegisterLink(@RequestParam @NotNull Long userId, @RequestParam @NotBlank @Length(min = 32, max = 32) String code) {
        log.debug("userId:{}, code:{}", userId, code);
        userService.checkRegisterLink(userId, code);
    }

    @GetMapping("emailCaptcha")
    @PublicInterface
    public void getEmailCaptcha(@RequestParam @Email String email, @RequestParam @NotEmpty String userName) {
        userService.getChangePasswordEmailCaptcha(email, userName);
    }

    @GetMapping("password-page")
    @PublicInterface
    public String checkChangeCode(@RequestParam @NotNull Long userId, @RequestParam @NotBlank @Length(min = 32, max = 32) String code) {
        return userService.checkChangePasswordMail(userId, code);
    }

    @PutMapping("password")
    public void changePassword(@RequestBody @Validated ChangePasswordReq req) {
        userService.changePassword(req.getPassword());
    }

    @PutMapping("settings")
    public void modifyUserSettings(@RequestBody @Validated SettingModifyReq req) {
        UserSettings userSettings = req.transform();
        userSettings.setUserId(TokenUtil.getUserId());
        this.userSettingsDao.updateByPrimaryKeySelective(userSettings);
    }

    @GetMapping("settings")
    public UserSettings getUserSettings() {
        return this.userService.getSettings();
    }
}
