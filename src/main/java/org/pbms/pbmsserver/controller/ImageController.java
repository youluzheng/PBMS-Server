package org.pbms.pbmsserver.controller;

import org.pbms.pbmsserver.common.auth.PublicInterface;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ResourceNotFoundException;
import org.pbms.pbmsserver.common.request.image.ImageUploadReq;
import org.pbms.pbmsserver.service.ImageService;
import org.pbms.pbmsserver.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ServerException;

/**
 * 图片接口
 *
 * @author 王俊
 * @author zyl
 * @date 2021-06-21
 */
@RestController
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@Validated ImageUploadReq imageUploadReq, @RequestBody MultipartFile image) {

        String responseContent = uploadService.uploadImage(imageUploadReq, image);
        return ResponseEntity.ok(responseContent);
    }

    @GetMapping("**/{image}")
    @PublicInterface
    public void getImage(@PathVariable String image, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.debug("获取图片:{}", image);

        // 去掉第一个'/'
        String path = request.getServletPath().substring(1);

        // 拼接图片路径
        try (FileInputStream in = new FileInputStream(ServerConstant.SERVER_ROOT_PATH + File.separator + path);
             ServletOutputStream outputStream = response.getOutputStream()) {
            String extension = FileUtil.getFileExt(image);
            response.setContentType("image/" + extension);
            byte[] data = new byte[in.available()];
            int count = in.read(data);
            if (count == 0) {
                throw new ServerException("读取空文件" + path);
            }
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found!");
        }
    }
}
