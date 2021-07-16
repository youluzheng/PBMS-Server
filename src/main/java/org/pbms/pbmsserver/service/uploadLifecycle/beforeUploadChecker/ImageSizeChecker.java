package org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadChecker;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageSizeChecker {
    private static final Logger log = LoggerFactory.getLogger(ImageSizeChecker.class);

    public void checkImageSize(final MultipartFile image) {
        log.debug("imageSize:{}, allowSize:{}", image.getSize(), ServerConstant.SERVER_MAX_SIZE);
        if (image.getSize() > ServerConstant.SERVER_MAX_SIZE) {
            throw new BusinessException(BusinessStatus.FILE_SIZE_OUT_OF_LIMIT);
        }
    }

}
