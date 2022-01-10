package org.pbms.pbmsserver.controller;


import org.pbms.pbmsserver.common.auth.AdminInterface;
import org.pbms.pbmsserver.common.request.user.UserListReq;
import org.pbms.pbmsserver.common.vo.PageData;
import org.pbms.pbmsserver.common.vo.user.UserListVO;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王俊
 * @author zyl
 * @since 0.3.0
 */
@RestController
@RequestMapping("user")
@Validated
public class UserManageController {

    @Autowired
    private UserManageService userManageService;

    @PatchMapping("{userId}/action-forbidden")
    @AdminInterface
    public void forbiddenUser(@PathVariable long userId) {
        userManageService.updateStatus(userId, UserStatusEnum.FORBID);
    }

    @PatchMapping("{userId}/action-unset")
    @AdminInterface
    public void unsetUser(@PathVariable long userId) {
        userManageService.updateStatus(userId, UserStatusEnum.NORMAL);
    }

    @PatchMapping("{userId}/action-audit")
    @AdminInterface
    public void auditUser(@PathVariable long userId, @RequestParam boolean pass) {
        userManageService.auditUser(userId, pass);
    }

    @DeleteMapping("{userId}")
    @AdminInterface
    public void deleteUser(@PathVariable long userId) {
        this.userManageService.deleteUser(userId);
    }

    @GetMapping("/list")
    @AdminInterface
    public PageData<UserListVO> userList(UserListReq req) {
        return this.userManageService.userList(req);
    }
}
