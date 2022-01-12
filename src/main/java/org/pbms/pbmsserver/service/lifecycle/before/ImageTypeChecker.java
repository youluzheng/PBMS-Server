package org.pbms.pbmsserver.service.lifecycle.before;

import cn.hutool.core.io.FileTypeUtil;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片类型检查
 *
 * @author zyl
 */
@Component
public class ImageTypeChecker {
    private static final Logger log = LoggerFactory.getLogger(ImageTypeChecker.class);

    public void checkImageType(final MultipartFile image) {
        String extension;
        try {
            extension = FileTypeUtil.getType(image.getInputStream());
        } catch (IOException e) {
            throw new ServerException();
        }
        log.debug("imageFileName:{}, extension:{}", image.getOriginalFilename(), extension);
        if (!ServerConstant.SERVER_SUPPORT_TYPE.contains(extension)) {
            throw new BusinessException(BusinessStatus.FILE_TYPE_NOT_SUPPORT);
        }
    }
}
