package org.pbms.pbmsserver.service.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.pbms.pbmsserver.common.constant.WaterMaskConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.service.WaterMaskService;
import org.pbms.pbmsserver.util.FileUtil;
import org.pbms.pbmsserver.util.FontUtil;
import org.pbms.pbmsserver.util.MultipartFileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WaterMaskServiceImpl implements WaterMaskService {

    @Override
    public MultipartFile addWaterMask(MultipartFile srcImg) {
        MultipartFile result = srcImg;
        if (!WaterMaskConstant.WATER_MASK_ENABLE) {
            return result;
        }
        if (WaterMaskConstant.WATER_MASK_LOGO_ENABLE) {
            result = addImgWaterMask(result);
        }
        if (WaterMaskConstant.WATER_MASK_ENABLE) {
            result = addTextWaterMask(result);
        }
        return result;
    }

    public MultipartFile addTextWaterMask(MultipartFile srcImg) {
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
        log.debug("开始对图片：{}绘制文字水印 “{}”", srcImg.getName(), WaterMaskConstant.WATER_MASK_TEXT);
        drawText(g, WaterMaskConstant.WATER_MASK_TEXT, bufferedImage);
        log.debug("水印绘制完成");
        return toMultipartFile(bufferedImage, tempFile);
    }

    public MultipartFile addImgWaterMask(MultipartFile srcImg) {
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
            imageLogo = ImageIO.read(new File(WaterMaskConstant.WATER_MASK_LOGO_PATH + File.separator + "logo.jpg"));
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        if (WaterMaskConstant.WATER_MASK_REPEAT) {
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
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, WaterMaskConstant.WATER_MASK_ALPHA));
        g.rotate(Math.toRadians(WaterMaskConstant.WATER_MASK_GRADIENT), bufferedImage.getWidth() / 2,
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
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, WaterMaskConstant.WATER_MASK_ALPHA));
        g.drawImage(imageLogo, bufferedImage.getWidth() - imageLogo.getWidth(),
                bufferedImage.getHeight() - imageLogo.getHeight(), null);
        g.dispose();
    }

    private void drawText(Graphics2D g, String text, BufferedImage bufferedImage) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, WaterMaskConstant.WATER_MASK_ALPHA));
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