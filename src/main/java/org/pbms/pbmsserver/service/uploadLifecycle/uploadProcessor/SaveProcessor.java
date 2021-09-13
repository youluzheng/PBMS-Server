package org.pbms.pbmsserver.service.uploadLifecycle.uploadProcessor;

import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.init.Init;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    public String upload(String fileName, MultipartFile image) {
        TokenBean tokenBean = TokenUtil.getTokenBean();
        UserSettings userSettings = this.userService.getSettings();
        // 服务器上存储路径
        String storagePath;

        StringBuilder imageURL = new StringBuilder(ServerConstant.SERVER_BASEURL).append("/")
                .append(Init.getRespectiveRelativeURLUploadPath(tokenBean)).append("/");

        // fileName中包含路径信息
        if (fileName == null || fileName.isBlank()) {
            storagePath = Init.getRespectiveAbsoluteUploadPath(tokenBean) + File.separator + image.getOriginalFilename();
            imageURL.append(image.getOriginalFilename());
        } else {
            storagePath = Init.getRespectiveAbsoluteUploadPath(tokenBean) + File.separator + fileName;
            imageURL.append(fileName);
        }

        File dest = new File(storagePath);
        if (!dest.exists()) {
            if (!dest.mkdirs()) {
                throw new ServerException("上传文件，文件夹创建失败," + storagePath);
            }
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
