package org.pbms.pbmsserver.controller.user;


import org.pbms.pbmsserver.common.auth.Role;
import org.pbms.pbmsserver.common.auth.RoleEnum;
import org.pbms.pbmsserver.common.request.user.UserListDTO;
import org.pbms.pbmsserver.common.response.PageVO;
import org.pbms.pbmsserver.common.response.user.UserListVO;
import org.pbms.pbmsserver.repository.enumeration.user.UserStatusEnum;
import org.pbms.pbmsserver.service.user.UserAdminService;
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
@Role(role = RoleEnum.ADMIN)
public class UserAdminController {

    @Autowired
    private UserAdminService userManageService;

    @PatchMapping("{userId}/action-forbidden")
    public void forbiddenUser(@PathVariable long userId) {
        userManageService.updateStatus(userId, UserStatusEnum.FORBID);
    }

    @PatchMapping("{userId}/action-unset")
    public void unsetUser(@PathVariable long userId) {
        userManageService.updateStatus(userId, UserStatusEnum.NORMAL);
    }

    @PatchMapping("{userId}/action-audit")
    public void auditUser(@PathVariable long userId, @RequestParam boolean pass) {
        userManageService.auditUser(userId, pass);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        this.userManageService.deleteUser(userId);
    }

    @GetMapping("page")
    public PageVO<UserListVO> userList(UserListDTO req) {
        return this.userManageService.userList(req);
    }
}
