package org.pbms.pbmsserver.common.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName CompressConstant
 * @Description 图片压缩常量配置
 * @Author 王俊
 * @Date 2021/7/10 14:39
 */
@Component
public class CompressConstant {
    private static final Logger log = LoggerFactory.getLogger(CompressConstant.class);

    public static boolean COMPRESS_ENABLE;
    public static double COMPRESS_SCALE;

    @Value("${compress.enable:false}")
    public void setCompressEnable(String enable) {
        enable = enable.toLowerCase();
        if(!enable.equals("true") && !enable.equals("false")){
            log.warn("compress-enable不支持配置:{}, 设置为默认值false", enable);
            enable = "false";
        }
        log.info("set compress-enable:{}", enable);
        CompressConstant.COMPRESS_ENABLE = Boolean.parseBoolean(enable);
    }

    @Value("${compress.scale:0.8}")
    public void setCompressScale(String scale) {
        try{
            double scaleDouble = Double.parseDouble(scale);
            if(scaleDouble > 1 || scaleDouble <= 0){
                log.warn("compress-scale不支持配置:{}, 设置为默认值0.8", scale);
                scale = "0.8";
            }
        }catch(NumberFormatException e){
            log.warn("compress-scale不支持配置:{}, 设置为默认值0.8", scale);
            scale = "0.8";
        }
        log.info("set compress-scale:{}", scale);
        CompressConstant.COMPRESS_SCALE = Double.parseDouble(scale);
    }
}
