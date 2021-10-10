package org.pbms.pbmsserver.common.request.user;


import org.pbms.pbmsserver.repository.model.UserInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author 王俊
 * @since 0.3.0
 */
public class UserRegisterReq {
    @NotBlank
    @Pattern(regexp = "[\\u4e00-\\u9fa5_a-zA-Z0-9_]{3,10}")
    private String userName;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @Email
    @NotBlank
    private String email;

    public UserRegisterReq() {

    }

    public UserInfo transform() {
        UserInfo user = new UserInfo();
        user.setUserName(this.userName);
        user.setPassword(this.password);
        user.setEmail(this.email);
        return user;
    }

    public UserRegisterReq(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
