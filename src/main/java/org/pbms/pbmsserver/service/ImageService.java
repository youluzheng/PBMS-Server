package org.pbms.pbmsserver.service;

import org.pbms.pbmsserver.common.exception.ClientException;
import org.pbms.pbmsserver.common.request.image.ImageUploadDTO;
import org.pbms.pbmsserver.service.lifecycle.after.ResponseProcessor;
import org.pbms.pbmsserver.service.lifecycle.before.CompressProcessor;
import org.pbms.pbmsserver.service.lifecycle.before.ImageSizeChecker;
import org.pbms.pbmsserver.service.lifecycle.before.ImageTypeChecker;
import org.pbms.pbmsserver.service.lifecycle.before.WaterMarkProcessor;
import org.pbms.pbmsserver.service.lifecycle.before.decode.FileNameDecodeProcessor;
import org.pbms.pbmsserver.service.lifecycle.upload.SaveProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传服务，目前实现的功能为压缩，水印，自定义返回类型
 *
 * @author 王俊
 * @author zyl
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

    public String uploadImage(ImageUploadDTO imageUploadReq, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ClientException("请选择上传文件");
        }
        // 1、前置检查
        this.imageSizeChecker.checkImageSize(image);
        this.imageTypeChecker.checkImageType(image);
        // 2、文件重命名
        String fileName = FileNameDecodeProcessor.of(imageUploadReq.getFileName(), image).process();
        // 3、添加水印
        image = this.waterMarkProcessor.addWaterMark(image);
        // 4、压缩图片，先添加水印后压缩避免水印被压掉
        image = this.compressProcessor.compress(image);
        // 5、生成http url
        String imageURL = this.saveProcessor.upload(fileName, image);
        // 6、根据配置选择url or markdown
        return this.responseProcessor.responseHandler(image, imageURL);
    }
}
