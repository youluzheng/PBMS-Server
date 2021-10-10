package org.pbms.pbmsserver.controller;

import cn.hutool.core.io.FileTypeUtil;
import org.pbms.pbmsserver.common.auth.PublicInterface;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.pbms.pbmsserver.common.exception.ResourceNotFoundException;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.common.request.image.ImageUploadReq;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.service.ImageService;
import org.pbms.pbmsserver.service.TempTokenService;
import org.pbms.pbmsserver.service.UserService;
import org.pbms.pbmsserver.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.List;

/**
 * 图片接口
 *
 * @author 王俊
 * @author zyl
 * @author zqs
 */
@RestController
@RequestMapping("image")
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService uploadService;
    @Autowired
    private TempTokenService tempTokenService;
    @Autowired
    private UserService userService;

    @PostMapping
    public String uploadImage(@Validated ImageUploadReq imageUploadReq, @RequestBody List<MultipartFile> image) {
        if (image == null || image.isEmpty()) {
            throw new ParamNullException(HttpStatus.BAD_REQUEST, "请选择上传文件");
        }
        StringBuilder sb = new StringBuilder();
        for (var img : image) {
            sb.append(uploadService.uploadImage(imageUploadReq, img)).append("\n");
        }
        return sb.toString();
    }

    @PostMapping("{token}")
    @PublicInterface
    public String uploadImageByTempToken(@PathVariable String token, @Validated ImageUploadReq imageUploadReq, @RequestBody List<MultipartFile> image) {
        if (image == null || image.isEmpty()) {
            throw new ParamNullException(HttpStatus.BAD_REQUEST, "请选择上传文件");
        }
        TempTokenInfo tempTokenInfo = tempTokenService.checkTempToken(token);
        tempTokenService.updateTimes(token, image.size());
        UserInfo userInfo = this.userService.getUserInfo(tempTokenInfo.getUserId());
        // 保存tokenBean信息, 使得和正常登录一致处理
        TokenUtil.setTokenBean(new TokenBean(userInfo.getUserId(), userInfo.getUserName(), userInfo.getRole()));
        StringBuilder sb = new StringBuilder();
        for (var img : image) {
            sb.append(uploadService.uploadImage(imageUploadReq, img)).append("\n");
        }
        return sb.toString();
    }

    @GetMapping("**/{image}")
    @PublicInterface
    public void getImage(@PathVariable String image, HttpServletRequest request, HttpServletResponse response) {
        // 去掉'/image'
        String path = request.getServletPath().substring(6);
        log.debug("path:{}", path);
        // 拼接图片路径
        try (FileInputStream in = new FileInputStream(ServerConstant.SERVER_ROOT_PATH + File.separator + path);
             ServletOutputStream outputStream = response.getOutputStream();
             FileInputStream in2 = new FileInputStream(ServerConstant.SERVER_ROOT_PATH + File.separator + path)
        ) {
            byte[] data = new byte[in.available()];
            int count = in.read(data);
            if (count == 0) {
                throw new ServerException("读取空文件" + path);
            }
            response.setContentType("image/" + FileTypeUtil.getType(in2));
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException("Resource Not Found!");
        } catch (IOException e) {
            throw new ServerException();
        }
    }
}
