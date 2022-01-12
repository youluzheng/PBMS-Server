package org.pbms.pbmsserver.common.request.tempToken;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 新增临时token请求类
 *
 * @author zqs
 */
@Data
public class TempTokenAddDTO {

    @NotNull
    private Date expireTime;

    @Min(1)
    @NotNull
    private Integer uploadTimes;

    private String note;
}
