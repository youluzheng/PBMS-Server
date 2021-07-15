package org.pbms.pbmsserver.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pbms.pbmsserver.common.constant.CompressConstant;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.util.FileUtil;
import org.pbms.pbmsserver.util.MultipartFileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

/**
 * @ClassName CompressServiceImpl
 * @Description 图片压缩业务
 * @Author 王俊
 * @Date 2021/7/10 14:31
 */
@Service
@Slf4j
public class CompressServiceImpl {

    // 缩略图后缀
    private static final String SUFFIX = "-thumbnail";

    private List<String> imageExtensions = Arrays.asList("jpeg", "jpg", "gif", "bmp", "png");

    /**
     * @param srcImg 原始图片
     * @return 压缩后图片，类型为MultipartFile
     * @throws IOException
     */
    public MultipartFile compress(MultipartFile srcImg, Boolean compress) {

        // 判断是否压缩，优先级：单次压缩>全局压缩
        Boolean isCompress = compress == null ? CompressConstant.COMPRESS_ENABLE : compress;
        if (!isCompress) {
            return srcImg;
        }
        File tempFile = new File(ServerConstant.SERVER_ROOT_PATH + File.separator + "temp" + File.separator
                + srcImg.getOriginalFilename());
        try {
            srcImg.transferTo(tempFile);
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        String compressPath = generateThumbnail2Directory(CompressConstant.COMPRESS_SCALE, tempFile.getParent(),
                tempFile.getAbsolutePath()).get(0);
        File compressImg = new File(compressPath);
        // 文件类型转换，方便后续处理
        MultipartFile result = MultipartFileUtil.fileToMultipartFile(compressImg);
        // 删除临时文件
        compressImg.delete();
        tempFile.delete();
        return result;
    }

    /**
     * 生成缩略图到指定的目录
     *
     * @param path  目录
     * @param files 要生成缩略图的文件列表
     * @throws IOException
     */
    private List<String> generateThumbnail2Directory(String path, String... files) {
        return generateThumbnail2Directory(CompressConstant.COMPRESS_SCALE, path, files);
    }

    /**
     * 生成缩略图到指定的目录
     *
     * @param scale    图片缩放率
     * @param pathname 缩略图保存目录
     * @param files    要生成缩略图的文件列表
     * @throws IOException
     */
    private List<String> generateThumbnail2Directory(double scale, String pathname, String... files) {
        try {
            Thumbnails.of(files)
                    // 图片缩放率，不能和size()一起使用
                    .scale(scale)
                    // 缩略图保存目录,该目录需存在，否则报错
                    .toFiles(new File(pathname), Rename.SUFFIX_HYPHEN_THUMBNAIL);
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        List<String> list = new ArrayList<>(files.length);
        for (String file : files) {
            list.add(appendSuffix(file, SUFFIX));
        }
        return list;
    }

    /**
     * 将指定目录下所有图片生成缩略图
     *
     * @param pathname 文件目录
     */
    private void generateDirectoryThumbnail(String pathname) {
        generateDirectoryThumbnail(pathname, CompressConstant.COMPRESS_SCALE);
    }

    /**
     * 将指定目录下所有图片生成缩略图
     *
     * @param pathname 文件目录
     */
    private void generateDirectoryThumbnail(String pathname, double scale) {
        File[] files = new File(pathname).listFiles();
        compressRecurse(files, pathname);
    }

    /**
     * 文件追加后缀
     *
     * @param fileName 原文件名
     * @param suffix   文件后缀
     * @return
     */
    private String appendSuffix(String fileName, String suffix) {
        String newFileName = "";

        int indexOfDot = fileName.lastIndexOf('.');

        if (indexOfDot != -1) {
            newFileName = fileName.substring(0, indexOfDot);
            newFileName += suffix;
            newFileName += fileName.substring(indexOfDot);
        } else {
            newFileName = fileName + suffix;
        }

        return newFileName;
    }

    private void compressRecurse(File[] files, String pathname) {
        for (File file : files) {
            // 目录
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                compressRecurse(subFiles, pathname + File.separator + file.getName());
            } else {
                // 文件包含压缩文件后缀或非图片格式，则不再压缩
                String extension = FileUtil.getFileExt(file);
                if (!file.getName().contains(SUFFIX) && isImage(extension)) {
                    generateThumbnail2Directory(pathname, file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 根据文件扩展名判断文件是否图片格式
     *
     * @param extension 文件扩展名
     * @return
     */
    private boolean isImage(String extension) {
        return this.imageExtensions.contains(extension.strip());
    }
}
