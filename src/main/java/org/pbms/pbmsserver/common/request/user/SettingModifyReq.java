package org.pbms.pbmsserver.common.request.user;

import org.pbms.pbmsserver.repository.model.UserSettings;

public class SettingModifyReq {

    private Boolean watermarkLogoEnable;

    private Boolean watermarkLogoRepeat;

    private Byte watermarkLogoGradient;

    private Byte watermarkLogoAlpha;

    private Boolean watermarkTextEnable;

    private String watermarkTextContent;

    private Byte watermarkTextAlpha;

    private Byte compressScale;

    private String responseReturnType;

    public UserSettings transfer() {
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
