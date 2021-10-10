package org.pbms.pbmsserver.common.request.user;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.pbms.pbmsserver.repository.model.UserSettings;

public class SettingModifyReq {

    private Boolean watermarkLogoEnable;

    private Boolean watermarkLogoRepeat;

    private Byte watermarkLogoGradient;

    @Range(min = 0, max = 100)
    private Byte watermarkLogoAlpha;

    private Boolean watermarkTextEnable;

    @Length(min = 1)
    private String watermarkTextContent;

    @Range(min = 0, max = 100)
    private Byte watermarkTextAlpha;

    @Range(min = 0, max = 100)
    private Byte compressScale;

    private String responseReturnType;

    public Boolean getWatermarkLogoEnable() {
        return watermarkLogoEnable;
    }

    public Boolean getWatermarkLogoRepeat() {
        return watermarkLogoRepeat;
    }

    public Byte getWatermarkLogoGradient() {
        return watermarkLogoGradient;
    }

    public Byte getWatermarkLogoAlpha() {
        return watermarkLogoAlpha;
    }

    public Boolean getWatermarkTextEnable() {
        return watermarkTextEnable;
    }

    public String getWatermarkTextContent() {
        return watermarkTextContent;
    }

    public Byte getWatermarkTextAlpha() {
        return watermarkTextAlpha;
    }

    public Byte getCompressScale() {
        return compressScale;
    }

    public String getResponseReturnType() {
        return responseReturnType;
    }

    public void setWatermarkLogoEnable(Boolean watermarkLogoEnable) {
        this.watermarkLogoEnable = watermarkLogoEnable;
    }

    public void setWatermarkTextEnable(Boolean watermarkTextEnable) {
        this.watermarkTextEnable = watermarkTextEnable;
    }

    public void setCompressScale(Byte compressScale) {
        this.compressScale = compressScale;
    }

    public void setResponseReturnType(String responseReturnType) {
        this.responseReturnType = responseReturnType;
    }

    public void setWatermarkLogoRepeat(Boolean watermarkLogoRepeat) {
        this.watermarkLogoRepeat = watermarkLogoRepeat;
    }

    public void setWatermarkLogoGradient(Byte watermarkLogoGradient) {
        this.watermarkLogoGradient = watermarkLogoGradient;
    }

    public void setWatermarkLogoAlpha(Byte watermarkLogoAlpha) {
        this.watermarkLogoAlpha = watermarkLogoAlpha;
    }

    public void setWatermarkTextContent(String watermarkTextContent) {
        this.watermarkTextContent = watermarkTextContent;
    }

    public void setWatermarkTextAlpha(Byte watermarkTextAlpha) {
        this.watermarkTextAlpha = watermarkTextAlpha;
    }

    public UserSettings transform() {
        UserSettings userSettings = new UserSettings();
        userSettings.setWatermarkLogoEnable(this.watermarkLogoEnable);
        if (this.watermarkLogoEnable != null && this.watermarkLogoEnable) {
            userSettings.setWatermarkLogoRepeat(this.watermarkLogoRepeat);
            userSettings.setWatermarkLogoGradient(this.watermarkLogoGradient);
            userSettings.setWatermarkLogoAlpha(this.watermarkLogoAlpha);
        }
        userSettings.setWatermarkTextEnable(this.watermarkTextEnable);
        if (this.watermarkTextEnable != null && this.watermarkLogoEnable) {
            userSettings.setWatermarkTextContent(this.watermarkTextContent);
            userSettings.setWatermarkTextAlpha(this.watermarkTextAlpha);
        }

        userSettings.setCompressScale(this.compressScale);
        userSettings.setResponseReturnType(this.responseReturnType);
        return userSettings;
    }
}
