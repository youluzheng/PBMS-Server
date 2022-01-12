package org.pbms.pbmsserver.common.request.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.pbms.pbmsserver.common.request.PageDTO;

/**
 * @author wangjun
 * @date 2022/1/10 13:41
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserListDTO extends PageDTO {
    private Byte status;

    public UserListDTO(int pageSize, int pageNo, Byte status) {
        super(pageSize, pageNo);
        this.status = status;
    }
}
