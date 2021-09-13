package org.pbms.pbmsserver.common.request.user;

import org.hibernate.validator.constraints.Length;
import org.pbms.pbmsserver.repository.model.UserSettings;

import javax.validation.constraints.Size;

public class SettingModifyReq {

    private Boolean watermarkLogoEnable;

    private Boolean watermarkLogoRepeat;

    private Byte watermarkLogoGradient;

    @Size(min = 0, max = 100)
    private Byte watermarkLogoAlpha;

    private Boolean watermarkTextEnable;

    @Length(min = 1)
    private String watermarkTextContent;

    @Size(min = 0, max = 100)
    private Byte watermarkTextAlpha;

    @Size(min = 0, max = 100)
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
