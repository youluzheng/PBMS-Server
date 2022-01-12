package org.pbms.pbmsserver.common.request.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.pbms.pbmsserver.common.request.PageReq;

/**
 * @author wangjun
 * @date 2022/1/10 13:41
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserListReq extends PageReq {
    private Byte status;

    public UserListReq(int pageSize, int pageNo, Byte status) {
        super(pageSize, pageNo);
        this.status = status;
    }
}
