package org.pbms.pbmsserver.service.lifecycle.after;

import cn.hutool.core.io.file.FileNameUtil;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 返回格式处理
 *
 * @author zyl
 */
@Component
public class ResponseProcessor {
    private static final Logger log = LoggerFactory.getLogger(ResponseProcessor.class);

    @Autowired
    private UserService userService;

    public String responseHandler(MultipartFile image, String url) {
        UserSettings userSettings = this.userService.getSettings();
        if ("url".equals(userSettings.getResponseReturnType())) {
            return url;
        } else if ("markdown".equals(userSettings.getResponseReturnType())) {
            String imageName = FileNameUtil.mainName(image.getOriginalFilename());
            return "![" + imageName + "](" + url + ")";
        } else {
            log.warn("异常responseType:{}, 默认返回url", userSettings.getResponseReturnType());
            return url;
        }
    }
}
