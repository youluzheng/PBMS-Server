package org.pbms.pbmsserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * File转MultipartFile工具类
 *
 * @author 王俊
 * @date 2021/7/10 14:31
 */
public class MultipartFileUtil {

    private static final Logger log = LoggerFactory.getLogger(MultipartFileUtil.class);

    public static MultipartFile fileToMultipartFile(File file) {
        FileItem fileItem = createFileItem(file);

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(file); OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("文件处理异常, {}", e.getMessage());
            throw new ServerException("文件处理异常");
        }
        return item;
    }
}
