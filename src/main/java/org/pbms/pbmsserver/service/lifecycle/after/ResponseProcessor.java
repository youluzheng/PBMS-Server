package org.pbms.pbmsserver.service.lifecycle.after;

import cn.hutool.core.io.file.FileNameUtil;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 返回格式处理
 *
 * @author zyl
 */
@Component
public class ResponseProcessor {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    public String responseHandler(MultipartFile image, String url) {
        UserSettings userSettings = this.userService.getSettings();
        if ("url".equals(userSettings.getResponseReturnType())) {
            return url;
        } else {
            String imageName = FileNameUtil.extName(image.getOriginalFilename());
            return "![" + imageName + "](" + url + ")";
        }
    }
}
