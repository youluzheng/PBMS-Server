package org.pbms.pbmsserver.common.auth;


/**
 * 用户类型，保存在token中，根据需要改动
 *
 * @author zyl
 */
public enum RoleEnum {
    ANONYMITY(0, "匿名用户"),
    NORMAL(1, "普通用户"),
    ADMIN(2, "管理员"),
    ALL_LOGGED_IN(3, "所有已登录的角色");

    private int code;

    public int getCode() {
        return code;
    }

    RoleEnum(int code, String description) {
        this.code = code;
    }
}
