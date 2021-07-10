package org.pbms.pbmsserver.service.impl;

import java.io.File;
import java.net.URI;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Override
    public ResponseEntity<String> Upload(String path, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择上传文件");
        }

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
            log.info("{}上传成功！", fullName);

            // 生成图片url
            StringBuilder imageURL = new StringBuilder();
            imageURL.append(ServerConstant.SERVER_BASEURL).append("/")
                    .append((path == null || "".equals(path)) ? "" : (path + "/")).append(fullName);
            log.debug(imageURL.toString());
            return ResponseEntity.created(new URI(ServerConstant.SERVER_BASEURL + fullName)).body(imageURL.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().body(fullName + "上传失败！");
    }

}
