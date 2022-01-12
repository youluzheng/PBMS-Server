package org.pbms.pbmsserver.common.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zyl
 */
@Data
@AllArgsConstructor
public class TokenBean {
    private static final Logger log = LoggerFactory.getLogger(TokenBean.class);

    private long userId;
    private String userName;
    private RoleEnum userRole;

    // 因为序列化默认是取得名字
    // 反序列化直接从string->RoleEnum
    public void setUserRole(String userRole) {
        this.userRole = RoleEnum.valueOf(userRole);
    }
}
