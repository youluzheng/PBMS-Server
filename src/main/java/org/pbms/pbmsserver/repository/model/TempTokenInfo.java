package org.pbms.pbmsserver.repository.model;

import java.util.Date;
import javax.annotation.Generated;

public class TempTokenInfo {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String secretKey;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Integer uploadTimes;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Date expireTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String note;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getId() {
        return id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setId(Long id) {
        this.id = id;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getSecretKey() {
        return secretKey;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Integer getUploadTimes() {
        return uploadTimes;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUploadTimes(Integer uploadTimes) {
        this.uploadTimes = uploadTimes;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Date getExpireTime() {
        return expireTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getNote() {
        return note;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", secretKey=").append(secretKey);
        sb.append(", uploadTimes=").append(uploadTimes);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", userId=").append(userId);
        sb.append(", note=").append(note);
        sb.append("]");
        return sb.toString();
    }
}