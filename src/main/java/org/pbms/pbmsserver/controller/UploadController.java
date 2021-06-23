package org.pbms.pbmsserver.controller;


import lombok.extern.slf4j.Slf4j;
import org.pbms.pbmsserver.common.GlobalConstant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

/**
 * @author 王俊
 * @Date 2021-06-21
 */
@RestController
@Slf4j
public class UploadController {
    @PostMapping("/image")
    public ResponseEntity<String> upload(@RequestParam(required = false) String path, @RequestBody MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择上传文件");
        }

        String fullName = image.getOriginalFilename();
        // 不带后缀文件名
        String fileName = fullName.substring(0, fullName.lastIndexOf("."));
        // 服务器上存储路径
        String storagePath;

        // 两种路径方式1：root+pictureName 2：root+path+pictureName
        if (path == null || "".equals(path)) {
            storagePath = GlobalConstant.ROOT_PATH + File.separator + fullName;
        } else {
            storagePath = GlobalConstant.ROOT_PATH + File.separator + path + File.separator + fullName;
            File parentPath = new File(GlobalConstant.ROOT_PATH + File.separator + path);
            if (!parentPath.exists()) {
                parentPath.mkdirs();
            }
        }
        File dest = new File(storagePath);
        try {
            image.transferTo(dest);
            log.info("上传成功！");

            // 拼接markdown图片格式
            StringBuilder mdFormatStr = new StringBuilder("![");
            mdFormatStr.append(fileName).append("](").append(GlobalConstant.BASEURL)
                    .append("/")
                    .append((path == null || "".equals(path)) ? "" : (path + "/"))
                    .append(fullName)
                    .append(")");

            return ResponseEntity.created(new URI(GlobalConstant.BASEURL + fullName)).body(mdFormatStr.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().body("上传失败！");
    }

    @GetMapping("**/{image}")
    public ResponseEntity<Void> getImage(@PathVariable String image, HttpServletRequest request, HttpServletResponse response) {
        // 去掉第一个'/'
        String path = request.getServletPath().substring(1);

        // 拼接图片路径
        try (FileInputStream in = new FileInputStream(GlobalConstant.ROOT_PATH + File.separator + path)) {
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
