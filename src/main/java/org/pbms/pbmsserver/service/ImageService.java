package org.pbms.pbmsserver.service;

import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.pbms.pbmsserver.service.uploadLifecycle.afterUploadProcessor.ResponseProcessor;
import org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadChecker.ImageSizeChecker;
import org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadChecker.ImageTypeChecker;
import org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadProcessor.CompressProcessor;
import org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadProcessor.WaterMarkProcessor;
import org.pbms.pbmsserver.service.uploadLifecycle.uploadProcessor.SaveProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传服务，目前实现的功能为压缩，水印，自定义返回类型
 *
 * @author 王俊
 * @date 2021/7/17 15:58
 */
@Component
public class ImageService {

    @Autowired
    private ImageSizeChecker imageSizeChecker;
    @Autowired
    private ImageTypeChecker imageTypeChecker;
    @Autowired
    private WaterMarkProcessor waterMarkProcessor;
    @Autowired
    private SaveProcessor saveProcessor;
    @Autowired
    private CompressProcessor compressProcessor;
    @Autowired
    private ResponseProcessor responseProcessor;

    public String uploadImage(String path, Boolean compress, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ParamNullException(HttpStatus.BAD_REQUEST, "请选择上传文件");
        }
        // 1、前置检查
        this.imageSizeChecker.checkImageSize(image);
        this.imageTypeChecker.checkImageType(image);
        // 2、添加水印
        image = this.waterMarkProcessor.addWaterMark(image);
        // 3、压缩图片，先添加水印后压缩避免水印被压掉
        image = this.compressProcessor.compress(image, compress);
        // 4、生成http url
        String imageURL = this.saveProcessor.upload(path, image);
        // 5、根据配置选择url or markdown
        return this.responseProcessor.responseHandler(image, imageURL);
    }
}
