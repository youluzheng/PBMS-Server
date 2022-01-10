package org.pbms.pbmsserver.common.vo.user;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangjun
 * @date 2022/1/10 10:26
 */
public class UserListVO {
    private Long userId;
    private String userName;
    private String email;
    private Byte status;
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Byte getStatus() {
        return status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserListVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
