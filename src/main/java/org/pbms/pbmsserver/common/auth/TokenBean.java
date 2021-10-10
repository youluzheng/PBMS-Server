package org.pbms.pbmsserver.common.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenBean {
    private static final Logger log = LoggerFactory.getLogger(TokenBean.class);

    private long userId;
    private String userName;
    private byte userRole;

    // 必须使用带参构造器，且只能有一个全参构造器，除非某些字段不需要
    // 如果使用set方法，以后字段增加时，需要找到所有的初始化代码，而且不会报错
    // 使用该方法可以使用ide的提示
    public TokenBean(long userId, String userName, byte userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte getUserRole() {
        return userRole;
    }

    public void setUserRole(byte userRole) {
        this.userRole = userRole;
    }
}
