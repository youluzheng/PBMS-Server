package org.pbms.pbmsserver.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangjun
 * @date 2022/1/10 13:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageReq {
    private int pageSize = 10;
    private int pageNo = 1;
}
