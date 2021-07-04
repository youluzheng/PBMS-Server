package org.pbms.pbmsserver.service;

import java.awt.*;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface WaterMaskService {
    
    /**
     * 在图片中下方添加文字水印
     * 
     * @param srcImg 原图
     */
    MultipartFile addTextWaterMask(MultipartFile srcImg) throws IOException, FontFormatException;
    
    /**
     * 在图片添加logo水印，可以选择是否重复，透明度，倾斜度（在配置文件中配） 不重复则在图片右下角生成单个水印
     * 
     * @param srcImg 原图
     */
    MultipartFile addImgWaterMask(MultipartFile srcImg) throws IOException;
}
