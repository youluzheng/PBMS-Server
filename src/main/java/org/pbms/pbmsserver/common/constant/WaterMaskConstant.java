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
    public static boolean WATER_MASK_TEXT_ENABLE;
    public static String WATER_MASK_TEXT;

    @Value("${watermask.enable:true}")
    public void setWaterMaskEnabled(String waterMaskEnabled) {
        waterMaskEnabled = waterMaskEnabled.toLowerCase();
        if(!waterMaskEnabled.equals("true") && !waterMaskEnabled.equals("false")){
            log.warn("watermask-enable不支持配置:{}, 设置为默认值true", waterMaskEnabled);
            waterMaskEnabled = "true";
        }
        log.info("set watermask-enable:{}", waterMaskEnabled);
        WaterMaskConstant.WATER_MASK_ENABLE = Boolean.parseBoolean(waterMaskEnabled);
    }

    @Value("${watermask.alpha:0.7}")
    public void setAlpha(String alpha) {
        try{
            double alphaDouble = Double.parseDouble(alpha);
            if(alphaDouble > 1 || alphaDouble < 0){
                log.warn("watermask-alpha不支持配置:{}, 设置为默认值0.7", alpha);
                alpha = "0.7";
            }
        }catch(NumberFormatException e){
            log.warn("watermask-alpha不支持配置:{}, 设置为默认值0.7", alpha);
            alpha = "0.7";
        }
        log.info("set watermask-alpha:{}", alpha);
        WaterMaskConstant.WATER_MASK_ALPHA = Float.parseFloat(alpha);
    }

    @Value("${watermask.logo.gradient:10}")
    public void setGradient(String gradient) {
        try{
            WaterMaskConstant.WATER_MASK_GRADIENT = Double.parseDouble(gradient);
        }catch(NumberFormatException e){
            log.warn("watermask-gradient不支持配置:{}, 设置为默认值10", gradient);
            gradient = "10";
            WaterMaskConstant.WATER_MASK_GRADIENT = Double.parseDouble(gradient);
        }
        log.info("set gradient:{}", gradient);
    }

    @Value("${watermask.logo.enable:false}")
    public void setLogo(String logo) {
        logo = logo.toLowerCase();
        if(!logo.equals("true") && !logo.equals("false")){
            log.warn("watermask-logo.enable不支持配置:{}, 设置为默认值false", logo);
            logo = "false";
        }
        log.info("set watermask-logo-enable:{}", logo);
        WaterMaskConstant.WATER_MASK_LOGO_ENABLE = Boolean.parseBoolean(logo);
    }

    @Value("${watermask.logo.repeat:true}")
    public void setRepeat(String repeat) {
        repeat = repeat.toLowerCase();
        if(!repeat.equals("true") && !repeat.equals("false")){
            log.warn("watermask-repeat.enable不支持配置:{}, 设置为默认值true", repeat);
            repeat = "true";
        }
        log.info("set logo-repeat:{}", repeat);
        WaterMaskConstant.WATER_MASK_REPEAT = Boolean.parseBoolean(repeat);
    }

    @Value("${watermask.logo.path:logo}")
    public void setLogoPath(String logoPath) {
        log.info("set logo-path:{}", logoPath);
        WaterMaskConstant.WATER_MASK_LOGO_PATH = logoPath;
    }

    @Value("${watermask.text.enable:false}")
    public void setWaterMaskEnable(String enable){
        enable = enable.toLowerCase();
        if(!enable.equals("true") && !enable.equals("false")){
            log.warn("watermask-text.enable不支持配置:{}, 设置为默认值false", enable);
            enable = "false";
        }
        log.info("set watermask.text-enable:{}", enable);
        WaterMaskConstant.WATER_MASK_TEXT_ENABLE = Boolean.parseBoolean(enable);
    }

    @Value("${watermask.text.content:nbnbnb}")
    public void setText(String text) {
        log.info("set text-content:{}", text);
        WaterMaskConstant.WATER_MASK_TEXT = text;
    }

}
