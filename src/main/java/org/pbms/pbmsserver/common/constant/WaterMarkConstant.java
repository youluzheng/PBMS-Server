package org.pbms.pbmsserver.common.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 水印常量配置类
 *
 * @author 王俊
 */
@Component
public class WaterMarkConstant {

    private static final Logger log = LoggerFactory.getLogger(WaterMarkConstant.class);

    public static boolean WATER_MARK_ENABLE;
    public static float WATER_MARK_ALPHA;
    public static double WATER_MARK_GRADIENT;
    public static boolean WATER_MARK_LOGO_ENABLE;
    public static boolean WATER_MARK_REPEAT;
    public static String WATER_MARK_LOGO_PATH;
    public static boolean WATER_MARK_TEXT_ENABLE;
    public static String WATER_MARK_TEXT;

    @Value("${watermark.enable:true}")
    public void setWaterMarkEnabled(String waterMarkEnabled) {
        waterMarkEnabled = waterMarkEnabled.toLowerCase();
        if (!"true".equals(waterMarkEnabled) && !"false".equals(waterMarkEnabled)) {
            log.warn("watermark-enable不支持配置:{}, 设置为默认值true", waterMarkEnabled);
            waterMarkEnabled = "true";
        }
        log.info("set watermark-enable:{}", waterMarkEnabled);
        WaterMarkConstant.WATER_MARK_ENABLE = Boolean.parseBoolean(waterMarkEnabled);
    }

    @Value("${watermark.alpha:0.7}")
    public void setAlpha(String alpha) {
        try {
            double alphaDouble = Double.parseDouble(alpha);
            if (alphaDouble > 1 || alphaDouble < 0) {
                log.warn("watermark-alpha不支持配置:{}, 设置为默认值0.7", alpha);
                alpha = "0.7";
            }
        } catch (NumberFormatException e) {
            log.warn("watermark-alpha不支持配置:{}, 设置为默认值0.7", alpha);
            alpha = "0.7";
        }
        log.info("set watermark-alpha:{}", alpha);
        WaterMarkConstant.WATER_MARK_ALPHA = Float.parseFloat(alpha);
    }

    @Value("${watermark.logo.gradient:10}")
    public void setGradient(String gradient) {
        try {
            WaterMarkConstant.WATER_MARK_GRADIENT = Double.parseDouble(gradient);
        } catch (NumberFormatException e) {
            log.warn("watermark-gradient不支持配置:{}, 设置为默认值10", gradient);
            gradient = "10";
            WaterMarkConstant.WATER_MARK_GRADIENT = Double.parseDouble(gradient);
        }
        log.info("set gradient:{}", gradient);
    }

    @Value("${watermark.logo.enable:false}")
    public void setLogo(String logo) {
        logo = logo.toLowerCase();
        if (!"true".equals(logo) && !"false".equals(logo)) {
            log.warn("watermark-logo.enable不支持配置:{}, 设置为默认值false", logo);
            logo = "false";
        }
        log.info("set watermark-logo-enable:{}", logo);
        WaterMarkConstant.WATER_MARK_LOGO_ENABLE = Boolean.parseBoolean(logo);
    }

    @Value("${watermark.logo.repeat:true}")
    public void setRepeat(String repeat) {
        repeat = repeat.toLowerCase();
        if (!"true".equals(repeat) && !"false".equals(repeat)) {
            log.warn("watermark-repeat.enable不支持配置:{}, 设置为默认值true", repeat);
            repeat = "true";
        }
        log.info("set logo-repeat:{}", repeat);
        WaterMarkConstant.WATER_MARK_REPEAT = Boolean.parseBoolean(repeat);
    }

    @Value("${watermark.logo.path:logo}")
    public void setLogoPath(String logoPath) {
        log.info("set logo-path:{}", logoPath);
        WaterMarkConstant.WATER_MARK_LOGO_PATH = logoPath;
    }

    @Value("${watermark.text.enable:false}")
    public void setWatermarkEnable(String enable) {
        enable = enable.toLowerCase();
        if (!"true".equals(enable) && !"false".equals(enable)) {
            log.warn("watermark-text.enable不支持配置:{}, 设置为默认值false", enable);
            enable = "false";
        }
        log.info("set watermark.text-enable:{}", enable);
        WaterMarkConstant.WATER_MARK_TEXT_ENABLE = Boolean.parseBoolean(enable);
    }

    @Value("${watermark.text.content:nbnbnb}")
    public void setText(String text) {
        log.info("set text-content:{}", text);
        WaterMarkConstant.WATER_MARK_TEXT = text;
    }

}
