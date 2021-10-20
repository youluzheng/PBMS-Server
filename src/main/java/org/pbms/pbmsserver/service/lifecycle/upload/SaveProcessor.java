package org.pbms.pbmsserver.service.lifecycle.upload;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 图片保存
 *
 * @author zyl
 * @date 2021/07/10 13:24:13
 */
@Component
public class SaveProcessor {
    private static final Logger log = LoggerFactory.getLogger(SaveProcessor.class);

    public String upload(String fileName, MultipartFile image) {
        // 服务器上存储路径
        String storagePath;

        StringBuilder imageURL = new StringBuilder(ServerConstant.getAbsoluteURLUploadPath()).append("/");

        // fileName中包含路径信息
        if (fileName == null || fileName.isBlank()) {
            storagePath = ServerConstant.getAbsoluteUploadPath() + "/" + image.getOriginalFilename();
            imageURL.append(image.getOriginalFilename());
        } else {
            storagePath = ServerConstant.getAbsoluteUploadPath() + "/" + fileName;
            imageURL.append(fileName);
        }

        File dest = new File(storagePath);
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new ServerException("上传文件，文件夹创建失败," + storagePath);
        }
        // 保存图片
        this.save(storagePath, dest, image);
        return imageURL.toString();
    }

    // TODO 将来可以适配OSS、七牛云等上传
    public void save(String storagePath, File dest, MultipartFile image) {
        try {
            image.transferTo(dest);
            log.info("{}上传成功！storagePath:{}", image.getOriginalFilename(), storagePath);
        } catch (Exception e) {
            log.error("上传失败, {}", e.getMessage());
            throw new ServerException(image.getOriginalFilename() + "上传失败！");
        }
    }
}
