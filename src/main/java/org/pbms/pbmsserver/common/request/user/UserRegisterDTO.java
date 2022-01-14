package org.pbms.pbmsserver.common.request.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pbms.pbmsserver.repository.model.UserInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author 王俊
 * @since 0.3.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @NotBlank
    @Pattern(regexp = "[\\u4e00-\\u9fa5_a-zA-Z0-9]{3,10}")
    private String userName;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @Email
    @NotBlank
    private String email;

    public UserInfo transform() {
        UserInfo user = new UserInfo();
        user.setUserName(this.userName);
        user.setPassword(this.password);
        user.setEmail(this.email);
        return user;
    }
}
