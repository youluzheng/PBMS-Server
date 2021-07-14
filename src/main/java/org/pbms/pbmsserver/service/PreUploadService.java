package org.pbms.pbmsserver.service;

import java.util.List;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.CustomCode;
import org.pbms.pbmsserver.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传时检查
 * 
 * @author zyl
 * @date 2021/07/12 20:08:46
 */
@Service
public class PreUploadService {
    private static final Logger log = LoggerFactory.getLogger(PreUploadService.class);

    private final long maxSize = ServerConstant.SERVER_MAX_SIZE;

    private final List<String> supportType = ServerConstant.SERVER_SUPPORT_TYPE;

    private void checkImageSize(final MultipartFile image) {
        log.debug("imageSize:{}, allowSize:{}", image.getSize(), maxSize);
        if (image.getSize() > this.maxSize) {
            throw new BusinessException(CustomCode.FILE_SIZE_OUT_OF_LIMIT);
        }
    }

    private void checkImageType(final MultipartFile image) {
        String extension = FileUtil.getFileExt(image);
        log.debug("imageFileName:{}, extension:{}", image.getOriginalFilename(), extension);
        if(!supportType.contains(extension)){
            throw new BusinessException(CustomCode.FILE_TYPE_NOT_SUPPORT);
        }
    }

    public void preUploadCheck(final MultipartFile image) {
        this.checkImageSize(image);
        this.checkImageType(image);
    }
}
