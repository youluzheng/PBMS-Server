package org.pbms.pbmsserver.common.vo.user;

import lombok.Data;

import java.util.Date;

/**
 * @author wangjun
 * @date 2022/1/10 10:26
 */
@Data
public class UserListVO {
    private Long userId;
    private String userName;
    private String email;
    private Byte status;
    private Date createTime;
}
