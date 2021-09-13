package org.pbms.pbmsserver.controller;

import org.pbms.pbmsserver.common.auth.PublicInterface;
import org.pbms.pbmsserver.common.request.user.SettingModifyReq;
import org.pbms.pbmsserver.common.request.user.UserLoginReq;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王俊
 * @author zyl
 * @date 2021/9/5 11:23
 * @since 0.3.0
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    @PublicInterface
    public String login(@Validated @RequestBody UserLoginReq req) {
        return this.userService.login(req.getUserName(), req.getPassword());
    }

    @PutMapping("settings")
    public void modifyUserSettings(@RequestBody @Validated SettingModifyReq req) {
        this.userService.modifySettings(req.transfer());
    }

    @GetMapping("settings")
    public UserSettings getUserSettings() {
        return this.userService.getSettings();
    }
}
