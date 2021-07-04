package org.pbms.pbmsserver.util;

import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FontUtil {
    /**
     * 宋体
     *
     * @param style
     * @param size
     */
    public static java.awt.Font getSIMSUN(int style, float size) throws java.awt.FontFormatException, IOException {
        java.awt.Font font = null;
        // 获取字体流
        try (InputStream simsunFontFile = FontUtil.class.getResourceAsStream("/fonts/PingFang-JianZhongHeiTi-2.ttf")) {
            // 创建字体
            font = java.awt.Font.createFont(java.awt.Font.PLAIN, simsunFontFile).deriveFont(style, size);
        } catch (java.awt.FontFormatException e) {
            log.error("字体文件异常 {}", e.getMessage());
            throw e;
        } catch (IOException e) {
            font = new java.awt.Font("宋体", java.awt.Font.BOLD, 6);
            log.error("字体文件未找到 {}", e.getMessage());
            throw e;
        }
        return font;
    }
}
