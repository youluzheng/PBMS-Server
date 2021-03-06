package org.pbms.pbmsserver.util;

import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * 水印字体工具类
 *
 * @author 王俊
 * @date 2021/7/10 14:39
 */
public final class FontUtil {

    private static final Logger log = LoggerFactory.getLogger(FontUtil.class);

    private FontUtil() {

    }

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
        } catch (FontFormatException e) {
            log.error("字体文件异常 {}", e.getMessage());
            throw new ServerException();
        } catch (IOException e) {
            font = new java.awt.Font("宋体", java.awt.Font.BOLD, 6);
            log.error("字体文件未找到 {}", e.getMessage());
            throw new ServerException();
        }
        return font;
    }
}
