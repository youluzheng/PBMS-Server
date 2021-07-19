package org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadProcessor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.pbms.pbmsserver.common.constant.WaterMarkConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.util.FileUtil;
import org.pbms.pbmsserver.util.FontUtil;
import org.pbms.pbmsserver.util.MultipartFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 添加水印服务
 *
 * @author 王俊
 * @date 2021/7/10 14:31
 */
@Component
public class WaterMarkProcessor {

    private static final Logger log = LoggerFactory.getLogger(WaterMarkProcessor.class);

    public MultipartFile addWaterMark(MultipartFile srcImg) {
        MultipartFile result = srcImg;
        if (!WaterMarkConstant.WATER_MARK_ENABLE) {
            return result;
        }
        if (WaterMarkConstant.WATER_MARK_LOGO_ENABLE) {
            result = addImgWaterMark(result);
        }
        if (WaterMarkConstant.WATER_MARK_ENABLE) {
            result = addTextWaterMark(result);
        }
        return result;
    }

    public MultipartFile addTextWaterMark(MultipartFile srcImg) {
        Graphics2D g;
        BufferedImage bufferedImage;
        File tempFile;
        Font font;

        try {
            font = FontUtil.getSIMSUN(Font.BOLD, 30);
            tempFile = new File(srcImg.getOriginalFilename());
            bufferedImage = ImageIO.read(srcImg.getInputStream());
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        } catch (FontFormatException e) {
            log.error("字体文件处理异常, {}", e.getMessage());
            throw new ServerException("字体文件处理异常");
        }

        g = bufferedImage.createGraphics();
        g.setColor(Color.BLACK);
        g.setFont(font);
        log.debug("开始对图片：{}绘制文字水印 “{}”", srcImg.getName(), WaterMarkConstant.WATER_MARK_TEXT);
        drawText(g, WaterMarkConstant.WATER_MARK_TEXT, bufferedImage);
        log.debug("水印绘制完成");
        return toMultipartFile(bufferedImage, tempFile);
    }

    public MultipartFile addImgWaterMark(MultipartFile srcImg) {
        Graphics2D g;
        BufferedImage bufferedImage;
        File tempFile;
        tempFile = new File(srcImg.getOriginalFilename());
        try {
            bufferedImage = ImageIO.read(srcImg.getInputStream());
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }

        g = bufferedImage.createGraphics();
        BufferedImage imageLogo;
        try {
            imageLogo = ImageIO.read(new File(WaterMarkConstant.WATER_MARK_LOGO_PATH + File.separator + "logo.jpg"));
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        if (WaterMarkConstant.WATER_MARK_REPEAT) {
            log.debug("开始对图片：{}绘制重复logo水印", srcImg.getName());
            drawRepeat(g, imageLogo, bufferedImage);
        } else {
            log.debug("开始对图片：{}绘制logo水印", srcImg.getName());
            drawImage(g, imageLogo, bufferedImage);
        }
        log.debug("水印绘制完成");
        return toMultipartFile(bufferedImage, tempFile);
    }

    private void drawRepeat(Graphics2D g, BufferedImage imageLogo, BufferedImage bufferedImage) {
        int markWidth = imageLogo.getWidth();
        int markHeight = imageLogo.getHeight();
        int xInterval = 50;
        int yInterval = 50;
        int x = 0;
        int y = 0;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double count = 2;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, WaterMarkConstant.WATER_MARK_ALPHA));
        g.rotate(Math.toRadians(WaterMarkConstant.WATER_MARK_GRADIENT), bufferedImage.getWidth() / 2,
                bufferedImage.getHeight() / 2);
        // 循环添加多个水印logo
        while (x < width * count) {
            y = -height / 2;
            while (y < height * count) {
                g.drawImage(imageLogo, x, y, null);
                y += markHeight + yInterval;
            }
            x += markWidth + xInterval;
        }
        g.dispose();
    }

    private void drawImage(Graphics2D g, BufferedImage imageLogo, BufferedImage bufferedImage) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, WaterMarkConstant.WATER_MARK_ALPHA));
        g.drawImage(imageLogo, bufferedImage.getWidth() - imageLogo.getWidth(),
                bufferedImage.getHeight() - imageLogo.getHeight(), null);
        g.dispose();
    }

    private void drawText(Graphics2D g, String text, BufferedImage bufferedImage) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, WaterMarkConstant.WATER_MARK_ALPHA));
        g.drawString(text, 10, 30);
    }

    private MultipartFile toMultipartFile(BufferedImage bufferedImage, File file) {
        String extension = FileUtil.getFileExt(file);
        try {
            ImageIO.write(bufferedImage, extension, file);
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        MultipartFile multipartFile = MultipartFileUtil.fileToMultipartFile(file);
        file.delete();
        return multipartFile;
    }
}