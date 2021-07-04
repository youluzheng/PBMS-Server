package org.pbms.pbmsserver.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WaterMaskConstant {

    public static boolean WATER_MASK_ENABLE;
    public static float WATER_MASK_ALPHA;
    public static double WATER_MASK_GRADIENT;
    public static boolean WATER_MASK_LOGO_ENABLE;
    public static boolean WATER_MASK_REPEAT;
    public static String WATER_MASK_LOGO_PATH;
    public static String WATER_MASK_TEXT;

    @Value("${watermask.enable:true}")
    public void setWaterMaskEnabled(String waterMaskEnabled) {
        log.info("set water-mask-enable:{}", waterMaskEnabled);
        WaterMaskConstant.WATER_MASK_ENABLE = Boolean.parseBoolean(waterMaskEnabled);
    }

    @Value("${watermask.alpha:70}")
    public void setAlpha(String alpha) {
        log.info("set alpha:{}", alpha);
        WaterMaskConstant.WATER_MASK_ALPHA = Float.valueOf(alpha);
    }

    @Value("${watermask.logo.gradient:10}")
    public void setGradient(String gradient) {
        log.info("set gradient:{}", gradient);
        WaterMaskConstant.WATER_MASK_GRADIENT = Double.valueOf(gradient);
    }

    @Value("${watermask.logo.enable:false}")
    public void setLogo(String logo) {
        log.info("set logo-enable:{}", logo);
        WaterMaskConstant.WATER_MASK_LOGO_ENABLE = Boolean.parseBoolean(logo);
    }

    @Value("${watermask.logo.repeat:true}")
    public void setRepeat(String repeat) {
        log.info("set logo-repeat:{}", repeat);
        WaterMaskConstant.WATER_MASK_REPEAT = Boolean.parseBoolean(repeat);
    }

    @Value("${watermask.logo.path}")
    public void setLogoPath(String logoPath) {
        log.info("set logo-path:{}", logoPath);
        WaterMaskConstant.WATER_MASK_LOGO_PATH = logoPath;
    }

    @Value("${watermask.text:nbnbnb}")
    public void setText(String text) {
        log.info("set text:{}", text);
        WaterMaskConstant.WATER_MASK_TEXT = text;
    }

}
