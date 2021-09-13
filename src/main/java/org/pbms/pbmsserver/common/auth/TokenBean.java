package org.pbms.pbmsserver.common.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenBean {
    private static final Logger log = LoggerFactory.getLogger(TokenBean.class);

    private Long userId;
    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
