package org.pbms.pbmsserver.repository.enumeration.user;

import org.pbms.pbmsserver.common.auth.RoleEnum;
import org.pbms.pbmsserver.common.exception.ServerException;

import java.util.Arrays;

/**
 * @author 王俊
 * @since 0.3.0
 */

public enum UserRoleEnum {
    ADMIN((byte) 1),
    NORMAL((byte) 2),
    ;

    private byte code;

    public byte getCode() {
        return code;
    }

    UserRoleEnum(byte code) {
        this.code = code;
    }

    /**
     * code转换为用户角色枚举类型
     *
     * @param code 代码
     * @return UserRoleEnum类型
     */
    private static UserRoleEnum valueOf(byte code) {
        return Arrays.stream(UserRoleEnum.values()).filter(x -> x.getCode() == code)
                .findFirst().orElseThrow(() -> new ServerException("code错误，code = " + code));
    }

    /**
     * 用户角色枚举类型转换成权限校验统一角色
     *
     * @return RoleEnum
     */
    public static RoleEnum transform(byte code) {
        return UserRoleEnum.valueOf(code).transform();
    }

    /**
     * 用户角色枚举类型转换成权限校验统一角色
     *
     * @return RoleEnum
     */
    public RoleEnum transform() {
        if (this == UserRoleEnum.NORMAL) {
            return RoleEnum.NORMAL;
        } else if (this == UserRoleEnum.ADMIN) {
            return RoleEnum.ADMIN;
        } else {
            throw new ServerException("枚举类错误");
        }
    }
}
