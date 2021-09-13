package org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadChecker;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.common.exception.ParamFormatException;
import org.pbms.pbmsserver.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片类型检查
 *
 * @author zyl
 * @date 2021/07/10 13:24:13
 */
@Component
public class ImageTypeChecker {
    private static final Logger log = LoggerFactory.getLogger(ImageTypeChecker.class);

    public void checkImageType(final MultipartFile image) {
        String extension;
        try {
            extension = FileUtil.getFileExt(image);
        } catch (ParamFormatException e) {
            throw new BusinessException(BusinessStatus.FILE_TYPE_NOT_SUPPORT);
        }
        log.debug("imageFileName:{}, extension:{}", image.getOriginalFilename(), extension);
        if (!ServerConstant.SERVER_SUPPORT_TYPE.contains(extension)) {
            throw new BusinessException(BusinessStatus.FILE_TYPE_NOT_SUPPORT);
        }
    }
}
