package org.pbms.pbmsserver.repository.model;

import javax.annotation.Generated;

public class UserSettings {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Long userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Boolean watermarkLogoEnable;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Boolean watermarkLogoRepeat;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte watermarkLogoGradient;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte watermarkLogoAlpha;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Boolean watermarkTextEnable;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String watermarkTextContent;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte watermarkTextAlpha;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private Byte compressScale;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    private String responseReturnType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Long getUserId() {
        return userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Boolean getWatermarkLogoEnable() {
        return watermarkLogoEnable;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkLogoEnable(Boolean watermarkLogoEnable) {
        this.watermarkLogoEnable = watermarkLogoEnable;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Boolean getWatermarkLogoRepeat() {
        return watermarkLogoRepeat;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkLogoRepeat(Boolean watermarkLogoRepeat) {
        this.watermarkLogoRepeat = watermarkLogoRepeat;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getWatermarkLogoGradient() {
        return watermarkLogoGradient;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkLogoGradient(Byte watermarkLogoGradient) {
        this.watermarkLogoGradient = watermarkLogoGradient;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getWatermarkLogoAlpha() {
        return watermarkLogoAlpha;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkLogoAlpha(Byte watermarkLogoAlpha) {
        this.watermarkLogoAlpha = watermarkLogoAlpha;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Boolean getWatermarkTextEnable() {
        return watermarkTextEnable;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkTextEnable(Boolean watermarkTextEnable) {
        this.watermarkTextEnable = watermarkTextEnable;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getWatermarkTextContent() {
        return watermarkTextContent;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkTextContent(String watermarkTextContent) {
        this.watermarkTextContent = watermarkTextContent;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getWatermarkTextAlpha() {
        return watermarkTextAlpha;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setWatermarkTextAlpha(Byte watermarkTextAlpha) {
        this.watermarkTextAlpha = watermarkTextAlpha;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public Byte getCompressScale() {
        return compressScale;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setCompressScale(Byte compressScale) {
        this.compressScale = compressScale;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String getResponseReturnType() {
        return responseReturnType;
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public void setResponseReturnType(String responseReturnType) {
        this.responseReturnType = responseReturnType;
    }

    @Override
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", watermarkLogoEnable=").append(watermarkLogoEnable);
        sb.append(", watermarkLogoRepeat=").append(watermarkLogoRepeat);
        sb.append(", watermarkLogoGradient=").append(watermarkLogoGradient);
        sb.append(", watermarkLogoAlpha=").append(watermarkLogoAlpha);
        sb.append(", watermarkTextEnable=").append(watermarkTextEnable);
        sb.append(", watermarkTextContent=").append(watermarkTextContent);
        sb.append(", watermarkTextAlpha=").append(watermarkTextAlpha);
        sb.append(", compressScale=").append(compressScale);
        sb.append(", responseReturnType=").append(responseReturnType);
        sb.append("]");
        return sb.toString();
    }
}