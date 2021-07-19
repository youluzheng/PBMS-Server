package org.pbms.pbmsserver.service.uploadLifecycle.afterUploadProcessor;

import org.pbms.pbmsserver.common.constant.ResponseConstant;
import org.pbms.pbmsserver.util.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 返回格式处理
 * 
 * @author zyl
 * @date 2021/07/10 13:24:13
 */
@Component
public class ResponseProcessor {
    private final String responseType = ResponseConstant.RESPONSE_TYPE;

    public String responseHandler(MultipartFile image, String url) {
        if (responseType.equals("url")) {
            return url;
        } else {
            String imageName = FileUtil.getFileNameWithoutExt(image);
            StringBuilder markdownStr = new StringBuilder();
            return markdownStr.append("![").append(imageName).append("](").append(url).append(")").toString();
        }
    }
}
