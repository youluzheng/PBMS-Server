package org.pbms.pbmsserver.common.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author wangjun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordReq {
    @NotBlank
    private String password;
}
