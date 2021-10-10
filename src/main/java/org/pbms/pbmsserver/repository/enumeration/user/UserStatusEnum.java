package org.pbms.pbmsserver.repository.enumeration.user;

import org.pbms.pbmsserver.common.exception.ClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum UserStatusEnum {
    NORMAL((byte) 1),
    FORBID((byte) 2),
    WAIT_FOR_AUDIT((byte) 3),
    AUDIT_FAIL((byte) 4),
    UNCHECKED((byte) 5);

    private byte code;

    public byte getCode() {
        return code;
    }

    public static UserStatusEnum getEnum(byte code) {
        for (UserStatusEnum v : values()) {
            if (v.code == code) {
                return v;
            }
        }
        throw new ClientException("用户枚举错误, code = " + code);
    }

    private static Map<UserStatusEnum, List<Byte>> legalTransferMap = new HashMap();

    static {
        UserStatusEnum.legalTransferMap.put(NORMAL, List.of(FORBID.code, WAIT_FOR_AUDIT.code, AUDIT_FAIL.code));
        UserStatusEnum.legalTransferMap.put(FORBID, List.of(NORMAL.code));
        UserStatusEnum.legalTransferMap.put(WAIT_FOR_AUDIT, List.of(UNCHECKED.code));
        UserStatusEnum.legalTransferMap.put(AUDIT_FAIL, List.of(WAIT_FOR_AUDIT.code));
    }

    /**
     * 获取符合要求的前置状态，如<code>code = 4</code>, 那么前置<code>code</code>必须是<code>3</code>
     *
     * @param afterStatus 转换后的状态
     * @return 符合要求的前置状态列表
     */
    public static List<Byte> getLegalStatus(UserStatusEnum afterStatus) {
        if (UserStatusEnum.legalTransferMap.containsKey(afterStatus)) {
            return UserStatusEnum.legalTransferMap.get(afterStatus);
        }
        throw new ClientException("状态错误, code = {}" + afterStatus.code);
    }

    UserStatusEnum(byte code) {
        this.code = code;
    }
}
