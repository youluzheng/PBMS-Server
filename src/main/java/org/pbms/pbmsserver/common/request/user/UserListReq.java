package org.pbms.pbmsserver.common.request.user;

import org.pbms.pbmsserver.common.request.PageReq;

/**
 * @author wangjun
 * @date 2022/1/10 13:41
 */
public class UserListReq extends PageReq {
    private Byte status;

    public UserListReq() {
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public UserListReq(Byte status) {
        this.status = status;
    }

    public UserListReq(int pageSize, int pageNo, Byte status) {
        super(pageSize, pageNo);
        this.status = status;
    }
}
