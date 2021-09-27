package org.pbms.pbmsserver.common.request.tempToken;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 新增临时token请求类
 *
 * @author zqs
 */
public class TempTokenAddReq {

    @NotNull
    private Date expireTime;

    @Min(1)
    @NotNull
    private Integer uploadTimes;

    private String note;

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getUploadTimes() {
        return uploadTimes;
    }

    public void setUploadTimes(Integer uploadTimes) {
        this.uploadTimes = uploadTimes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
