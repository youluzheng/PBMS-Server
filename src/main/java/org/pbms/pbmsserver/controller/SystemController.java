package org.pbms.pbmsserver.controller;


import org.pbms.pbmsserver.common.request.user.UserRegisterDTO;
import org.pbms.pbmsserver.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统管理相关接口
 *
 * @author 王俊
 * @author zyl
 * @since 0.3.0
 */
@RestController
@RequestMapping("system")
public class SystemController {
    @Autowired
    private SystemService systemManageService;

    @PostMapping("admin")
    public void initAdmin(@RequestBody @Validated UserRegisterDTO req) {
        systemManageService.initAdmin(req);
    }

    @GetMapping("init")
    public String checkInit() {
        // 以有没有用户作为判断
        return String.valueOf(systemManageService.isInit());
    }
}
