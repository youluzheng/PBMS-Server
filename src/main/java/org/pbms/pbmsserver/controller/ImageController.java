package org.pbms.pbmsserver.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ResourceNotFoundException;
import org.pbms.pbmsserver.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 王俊
 * @author zyl
 * @date 2021-06-21
 */
@RestController
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam(required = false) String path,
            @RequestParam(required = false) Boolean compress, @RequestBody MultipartFile image) {

        String responseContent = uploadService.uploadImage(path, compress, image);
        return ResponseEntity.ok(responseContent);
    }

    @GetMapping("**/{image}")
    public void getImage(@PathVariable String image, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.debug("获取图片:{}", image);

        // 去掉第一个'/'
        String path = request.getServletPath().substring(1);

        // 拼接图片路径
        try (FileInputStream in = new FileInputStream(ServerConstant.SERVER_ROOT_PATH + File.separator + path);
                ServletOutputStream outputStream = response.getOutputStream();) {
            byte[] data = new byte[in.available()];
            in.read(data);
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            log.error(image + ":文件不存在");
            throw new ResourceNotFoundException("Resource Not Found!");
        }
    }
}
