package org.pbms.pbmsserver.common.request.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.pbms.pbmsserver.repository.model.UserSettings;

@Data
public class SettingModifyDTO {

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

    public UserSettings transform() {
        UserSettings userSettings = new UserSettings();
        userSettings.setWatermarkLogoEnable(this.watermarkLogoEnable);
        if (this.watermarkLogoEnable != null && this.watermarkLogoEnable) {
            userSettings.setWatermarkLogoRepeat(this.watermarkLogoRepeat);
            userSettings.setWatermarkLogoGradient(this.watermarkLogoGradient);
            userSettings.setWatermarkLogoAlpha(this.watermarkLogoAlpha);
        }
        userSettings.setWatermarkTextEnable(this.watermarkTextEnable);
        if (this.watermarkTextEnable != null && this.watermarkTextEnable) {
            userSettings.setWatermarkTextContent(this.watermarkTextContent);
            userSettings.setWatermarkTextAlpha(this.watermarkTextAlpha);
        }

        userSettings.setCompressScale(this.compressScale);
        userSettings.setResponseReturnType(this.responseReturnType);
        return userSettings;
    }
}
