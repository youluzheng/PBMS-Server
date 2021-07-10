package org.pbms.pbmsserver.service;

import org.pbms.pbmsserver.common.constant.ResponseConstant;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 返回类型处理
 * 
 * @author zyl
 * @date 2021/07/10 13:24:13
 */
@Service
public class ResponseService {
    private final String responseType = ResponseConstant.RESPONSE_TYPE;

    public String responseHandler(MultipartFile image, String url) {
        if (responseType.equals("url")) {
            return url;
        } else {
            String fullName = image.getOriginalFilename();
            String imageName = fullName.substring(0, fullName.lastIndexOf("."));
            StringBuilder markdownStr = new StringBuilder();
            return markdownStr.append("![").append(imageName).append("](").append(url).append(")").toString();
        }
    }
}
