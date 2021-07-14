package org.pbms.pbmsserver.util;

import java.io.File;
import java.util.Objects;

import org.pbms.pbmsserver.common.exception.ParamFormatException;
import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * 
 * @author zyl
 * @date 2021/07/12 20:31:23
 */
public class FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 从文件名称中获取不带后缀的文件名称
     * <p>
     * <code>test.png -> test</code>
     * <p>
     * <code>abc -> abc</code>
     * <p>
     * <code>a/b/c/abc.pdf -> abc</code>
     * <p>
     * <code>a/b/c/abc.d.e -> abc</code>
     * <p>
     * <code>a/b/c/abc -> abc</code>
     * 
     * @param fullName 文件完整名称
     * @return 不带后缀的文件名称
     * @throws NullPointerException 如果<code>fullName == null</code>
     * @throws ParamNullException 如果<code>fullName</code>为空或仅由空白字符组成
     * @throws ParamFormatException 如果<code>fullName</code>不是<code>[\u4e00-\u9fa5_a-zA-Z0-9]+</code>结尾
     */
    public static String getFileNameWithoutExt(String fullName) {
        Objects.requireNonNull(fullName);
        if(fullName.isBlank()){
            log.error("throw ParamNullException, fullName:{}", fullName);
            throw new ParamNullException(HttpStatus.INTERNAL_SERVER_ERROR ,"fullName require not empty but got [" + fullName + "]");
        }
        boolean isValidFormat = fullName.matches("^.*[\u4e00-\u9fa5_a-zA-Z0-9]$");
        if(!isValidFormat){
            log.error("throw ParamFormatException, fullName:{}", fullName);
            throw new ParamFormatException(HttpStatus.INTERNAL_SERVER_ERROR ,"fullName format error [" + fullName + "]");
        }
        String fileName = fullName;
        int lastSplitLineIndex = fileName.lastIndexOf("/");
        if (lastSplitLineIndex != -1) {
            fileName= fileName.substring(lastSplitLineIndex + 1);
        }
        lastSplitLineIndex = fileName.lastIndexOf("\\");
        if (lastSplitLineIndex != -1) {
            fileName= fileName.substring(lastSplitLineIndex + 1);
        }
        int firstDotIndex = fileName.indexOf(".");
        if (firstDotIndex != -1) {
            fileName= fileName.substring(0, firstDotIndex);
        }
        return fileName;
    }

    /**
     * 从文件名称中获取不带后缀的文件名称
     * 
     * @param file 文件
     * @return 不带后缀的文件名称 
     * @throws NullPointerException 如果<code>file == null</code>
     */
    public static String getFileNameWithoutExt(File file){
        Objects.requireNonNull(file);
        return FileUtil.getFileNameWithoutExt(file.getAbsolutePath());
    }

    /**
     * 从文件名称中获取不带后缀的文件名称
     * 
     * @param file 文件
     * @return 不带后缀的文件名称 
     * @throws NullPointerException 如果<code>file == null</code>
     * @throws ParamNullException 如果<code>file.isEmpty() == true</code>
     */
    public static String getFileNameWithoutExt(MultipartFile file){
        Objects.requireNonNull(file);
        if(file.isEmpty()){
            log.error("throw ParamNullException, file is empty");
            throw new ParamNullException(HttpStatus.INTERNAL_SERVER_ERROR, "file require not empty");
        }
        return FileUtil.getFileNameWithoutExt(file.getOriginalFilename());
    }

    /**
     * 从文件名称中获取文件后缀
     * <p>
     * <code>test.png -> png</code>
     * <p>
     * <code>a/b/c/abc.pdf -> pdf</code>
     * <p>
     * <code>a/b/c/abc.d.e -> d.e</code>
     * 
     * @param fullName 文件完整名称
     * @return 文件的后缀
     * @throws NullPointerException 如果<code>fullName == null</code>
     * @throws ParamNullException 如果<code>fullName</code>为空或仅由空白字符组成
     * @throws ParamFormatException 如果<code>fullName</code>不是<code>.[\u4e00-\u9fa5_a-zA-Z0-9]+</code>结尾
     */
    public static String getFileExt(String fullName){
        Objects.requireNonNull(fullName);
        if(fullName.isBlank()){
            log.error("throw ParamFormatException, fullName:{}", fullName);
            throw new ParamNullException(HttpStatus.INTERNAL_SERVER_ERROR ,"fullName require not empty but got [" + fullName + "]");
        }
        boolean isValidFormat = fullName.matches("^.*[\u4e00-\u9fa5_a-zA-Z0-9]\\.[\u4e00-\u9fa5_a-zA-Z0-9]+$");
        if(!isValidFormat){
            log.error("throw ParamFormatException, fullName:{}", fullName);
            throw new ParamFormatException(HttpStatus.INTERNAL_SERVER_ERROR ,"fullName format error [" + fullName + "]");
        }
        // XXX zyl 使用文件二进制判断文件类型
        String extension = fullName;
        int lastSplitLineIndex = extension.lastIndexOf("/");
        if (lastSplitLineIndex != -1) {
            extension = extension.substring(lastSplitLineIndex + 1);
        }
        lastSplitLineIndex = extension.lastIndexOf("\\");
        if (lastSplitLineIndex != -1) {
            extension = extension.substring(lastSplitLineIndex + 1);
        }
        int firstDotIndex = extension.indexOf(".");
        if (firstDotIndex != -1) {
            extension = extension.substring(firstDotIndex + 1);
        }
        return extension;
    }

    /**
     * 从文件名称中获取文件后缀
     * 
     * @param file 文件
     * @return 文件的后缀
     * @throws NullPointerException 如果<code>file == null</code>
     */
    public static String getFileExt(File file){
        Objects.requireNonNull(file);
        return FileUtil.getFileExt(file.getAbsolutePath());
    }

    /**
     * 从文件名称中获取文件后缀
     * 
     * @param file 文件
     * @return 文件的后缀
     * @throws NullPointerException 如果<code>file == null</code>
     * @throws ParamNullException 如果<code>file.isEmpty() == true</code>
     */
    public static String getFileExt(MultipartFile file){
        Objects.requireNonNull(file);
        if(file.isEmpty()){
            log.error("throw ParamNullException, file is empty");
            throw new ParamNullException(HttpStatus.INTERNAL_SERVER_ERROR, "file require not empty");
        }
        return FileUtil.getFileExt(file.getOriginalFilename());
    }
}
