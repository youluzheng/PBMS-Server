package org.pbms.pbmsserver.repository.enumeration.user;

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
}
