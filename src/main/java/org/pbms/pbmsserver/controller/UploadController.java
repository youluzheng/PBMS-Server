package org.pbms.pbmsserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.service.PreUploadService;
import org.pbms.pbmsserver.service.ResponseService;
import org.pbms.pbmsserver.service.UploadService;
import org.pbms.pbmsserver.service.WaterMaskService;
import org.pbms.pbmsserver.service.impl.CompressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 王俊
 * @Date 2021-06-21
 */
@RestController
@Slf4j
public class UploadController {
    @Autowired
    private PreUploadService preUploadService;
    @Autowired
    private WaterMaskService waterMaskService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private CompressServiceImpl compressService;
    @Autowired
    private ResponseService responseService;

    @PostMapping("/image")
    public ResponseEntity<String> upload(@RequestParam(required = false) String path,
                                         @RequestParam(required = false) Boolean compress,
                                         @RequestBody MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择上传文件");
        }
        // 前置检查
        preUploadService.preUploadCheck(image);
        // 压缩图片
        MultipartFile result = compressService.compress(image, compress);
        // 添加水印
        result = waterMaskService.addWaterMask(result);
        ResponseEntity<String> uploadResult = uploadService.upload(path, result);
        String responseContent = this.responseService.responseHandler(image, uploadResult.getBody());
        return new ResponseEntity<>(responseContent, uploadResult.getStatusCode());
    }

    @GetMapping("**/{image}")
    public ResponseEntity<Void> getImage(@PathVariable String image, HttpServletRequest request,
                                         HttpServletResponse response) {
        // 去掉第一个'/'
        String path = request.getServletPath().substring(1);

        // 拼接图片路径
        try (FileInputStream in = new FileInputStream(ServerConstant.SERVER_ROOT_PATH + File.separator + path)) {
            byte[] data = new byte[in.available()];
            in.read(data);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).build();
        } catch (FileNotFoundException e) {
            log.error(image + ":文件不存在");
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("获取文件失败");
        }
        return ResponseEntity.internalServerError().build();
    }
}
