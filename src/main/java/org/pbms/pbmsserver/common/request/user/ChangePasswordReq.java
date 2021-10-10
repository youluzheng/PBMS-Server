package org.pbms.pbmsserver.common.request.user;

import javax.validation.constraints.NotBlank;

/**
 * @author wangjun
 */
public class ChangePasswordReq {
    @NotBlank
    private String password;

    public ChangePasswordReq(String password) {
        this.password = password;
    }

    public ChangePasswordReq() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
