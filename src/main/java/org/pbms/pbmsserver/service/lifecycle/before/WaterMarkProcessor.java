package org.pbms.pbmsserver.service.lifecycle.before;

import cn.hutool.core.io.FileTypeUtil;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.pbms.pbmsserver.util.FontUtil;
import org.pbms.pbmsserver.util.MultipartFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 添加水印服务
 *
 * @author 王俊
 * @date 2021/7/10 14:31
 */
@Component
public class WaterMarkProcessor {

    private static final Logger log = LoggerFactory.getLogger(WaterMarkProcessor.class);

    @Autowired
    private UserService userService;

    public MultipartFile addWaterMark(MultipartFile srcImg) {
        UserSettings userSettings = this.userService.getSettings();
        MultipartFile result = srcImg;
        if (Boolean.TRUE.equals(userSettings.getWatermarkLogoEnable())) {
            result = addImgWaterMark(result);
        }
        if (Boolean.TRUE.equals(userSettings.getWatermarkTextEnable())) {
            result = addTextWaterMark(result);
        }
        return result;
    }

    public MultipartFile addTextWaterMark(MultipartFile srcImg) {
        UserSettings userSettings = this.userService.getSettings();
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
        log.debug("开始对图片：{}绘制文字水印 “{}”", srcImg.getName(), userSettings.getWatermarkTextContent());
        drawText(g, userSettings.getWatermarkTextContent());
        log.debug("水印绘制完成");
        return toMultipartFile(bufferedImage, tempFile);
    }

    public MultipartFile addImgWaterMark(MultipartFile srcImg) {
        UserSettings userSettings = this.userService.getSettings();
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
            imageLogo = ImageIO.read(new File(ServerConstant.getAbsoluteLogoPath() + File.separator + "logo.jpg"));
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        if (Boolean.TRUE.equals(userSettings.getWatermarkLogoRepeat())) {
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
        UserSettings userSettings = this.userService.getSettings();
        int markWidth = imageLogo.getWidth();
        int markHeight = imageLogo.getHeight();
        int xInterval = 50;
        int yInterval = 50;
        int x = 0;
        int y = 0;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double count = 2;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, userSettings.getWatermarkLogoAlpha() / 100F));
        g.rotate(Math.toRadians(userSettings.getWatermarkLogoGradient()), bufferedImage.getWidth() / 2.0,
                bufferedImage.getHeight() / 2.0);
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
        UserSettings userSettings = this.userService.getSettings();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, userSettings.getWatermarkLogoAlpha() / 100F));
        g.drawImage(imageLogo, bufferedImage.getWidth() - imageLogo.getWidth(),
                bufferedImage.getHeight() - imageLogo.getHeight(), null);
        g.dispose();
    }

    private void drawText(Graphics2D g, String text) {
        UserSettings userSettings = this.userService.getSettings();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, userSettings.getWatermarkTextAlpha() / 100F));
        g.drawString(text, 10, 30);
    }

    private MultipartFile toMultipartFile(BufferedImage bufferedImage, File file) {
        String extension = FileTypeUtil.getType(file);
        try {
            ImageIO.write(bufferedImage, extension, file);
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        MultipartFile multipartFile = MultipartFileUtil.fileToMultipartFile(file);
        try {
            Files.delete(file.toPath());
        } catch (Exception e) {
            log.warn("文件删除失败, {}", e.getMessage());
        }
        return multipartFile;
    }
}