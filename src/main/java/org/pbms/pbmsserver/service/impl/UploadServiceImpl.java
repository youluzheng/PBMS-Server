package org.pbms.pbmsserver.service.impl;

import java.io.File;
import java.net.URI;

import org.pbms.pbmsserver.common.GlobalConstant;
import org.pbms.pbmsserver.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService{

    @Override
    public ResponseEntity<String> Upload(String path, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择上传文件");
        }

        String fullName = image.getOriginalFilename();
        // 不带后缀文件名
        String fileName = fullName.substring(0, fullName.lastIndexOf("."));
        // 服务器上存储路径
        String storagePath;

        // 两种路径方式1：root+pictureName 2：root+path+pictureName
        if (path == null || "".equals(path)) {
            storagePath = GlobalConstant.ROOT_PATH + File.separator + fullName;
        } else {
            storagePath = GlobalConstant.ROOT_PATH + File.separator + path + File.separator + fullName;
            File parentPath = new File(GlobalConstant.ROOT_PATH + File.separator + path);
            if (!parentPath.exists()) {
                parentPath.mkdirs();
            }
        }
        File dest = new File(storagePath);
        try {
            image.transferTo(dest);
            log.info("{}上传成功！", fullName);

            // 拼接markdown图片格式
            StringBuilder mdFormatStr = new StringBuilder("![");
            mdFormatStr.append(fileName).append("](").append(GlobalConstant.BASEURL).append("/")
                    .append((path == null || "".equals(path)) ? "" : (path + "/")).append(fullName).append(")");
            log.debug(mdFormatStr.toString());
            return ResponseEntity.created(new URI(GlobalConstant.BASEURL + fullName)).body(mdFormatStr.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().body(fullName + "上传失败！");
    }

}
