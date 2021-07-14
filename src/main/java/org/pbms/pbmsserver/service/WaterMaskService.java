package org.pbms.pbmsserver.service;

import java.awt.*;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface WaterMaskService {

    /**
     * 添加水印，支持文字水印和logo水印
     * @param srcImg
     * @return
     */
    public MultipartFile addWaterMask(MultipartFile srcImg);
}
