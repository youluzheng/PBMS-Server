package org.pbms.pbmsserver.common.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginReq {
    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;
}
