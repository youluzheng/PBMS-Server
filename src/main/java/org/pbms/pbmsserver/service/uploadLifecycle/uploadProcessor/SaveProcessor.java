package org.pbms.pbmsserver.service.uploadLifecycle.uploadProcessor;

import java.io.File;

import org.apache.logging.log4j.util.Strings;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

        StringBuilder imageURL = new StringBuilder(ServerConstant.SERVER_BASEURL).append("/");

        // fileName中包含路径信息
        if(fileName == null || fileName.isBlank()){
            storagePath = ServerConstant.SERVER_ROOT_PATH + File.separator + image.getOriginalFilename();
            imageURL.append(image.getOriginalFilename());
        }else{
            storagePath = ServerConstant.SERVER_ROOT_PATH + File.separator + fileName;
            imageURL.append(fileName);
        }

        File dest = new File(storagePath);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        try {
            image.transferTo(dest);
            log.info("{}上传成功！storagePath:{}", image.getOriginalFilename(), storagePath);
        } catch (Exception e) {
            log.error("上传失败, {}", e.getMessage());
            throw new ServerException(image.getOriginalFilename() + "上传失败！");
        }

        return imageURL.toString();
    }
}
