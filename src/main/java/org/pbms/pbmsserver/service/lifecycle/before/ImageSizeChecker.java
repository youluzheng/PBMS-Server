package org.pbms.pbmsserver.service.lifecycle.before;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片大小检查
 *
 * @author zyl
 * @date 2021/07/10 13:24:13
 */
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
