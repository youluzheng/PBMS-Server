package org.pbms.pbmsserver.repository.enumeration.user;

public enum UserStatusEnum {
    NORMAL((byte) 1),
    FORBID((byte) 2),
    WAIT_FOR_AUDIT((byte) 3),
    AUDIT_FAIL((byte) 4);

    private byte code;

    public byte getCode() {
        return code;
    }

    UserStatusEnum(byte code) {
        this.code = code;
    }
}
