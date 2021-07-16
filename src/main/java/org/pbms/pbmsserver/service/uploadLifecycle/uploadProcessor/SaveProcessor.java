package org.pbms.pbmsserver.service.uploadLifecycle.uploadProcessor;

import java.io.File;

import org.apache.logging.log4j.util.Strings;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class SaveProcessor {
    private static final Logger log = LoggerFactory.getLogger(SaveProcessor.class);

    private String baseURL = ServerConstant.SERVER_BASEURL;

    public String upload(String path, MultipartFile image) {
        String fullName = image.getOriginalFilename();
        // 服务器上存储路径
        String storagePath;

        // 两种路径方式1：root+pictureName 2：root+path+pictureName
        if (path == null || "".equals(path)) {
            storagePath = ServerConstant.SERVER_ROOT_PATH + File.separator + fullName;
        } else {
            storagePath = ServerConstant.SERVER_ROOT_PATH + File.separator + path + File.separator + fullName;
            File parentPath = new File(ServerConstant.SERVER_ROOT_PATH + File.separator + path);
            if (!parentPath.exists()) {
                parentPath.mkdirs();
            }
        }
        File dest = new File(storagePath);
        try {
            image.transferTo(dest);
            log.info("{}上传成功！path:{}", fullName, path);
        } catch (Exception e) {
            log.error("上传失败, {}", e.getMessage());
            throw new ServerException(fullName + "上传失败！");
        }
        StringBuilder imageURL = new StringBuilder(this.baseURL);
        imageURL.append("/");
        if (!Strings.isBlank(path)) {
            imageURL.append(path).append("/");
        }
        imageURL.append(fullName);
        return imageURL.toString();
    }
}
