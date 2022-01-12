package org.pbms.pbmsserver.controller.image;

import org.pbms.pbmsserver.common.auth.Role;
import org.pbms.pbmsserver.common.auth.RoleEnum;
import org.pbms.pbmsserver.common.exception.ClientException;
import org.pbms.pbmsserver.common.request.image.ImageUploadDTO;
import org.pbms.pbmsserver.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("image")
@Role(role = RoleEnum.ALL_LOGGED_IN)
public class ImageUserController {
    @Autowired
    private ImageService uploadService;

    @PostMapping
    public String uploadImage(@Validated ImageUploadDTO imageUploadReq, @RequestBody List<MultipartFile> image) {
        if (image == null || image.isEmpty()) {
            throw new ClientException("请选择上传文件");
        }
        StringBuilder sb = new StringBuilder();
        for (var img : image) {
            sb.append(uploadService.uploadImage(imageUploadReq, img)).append("\n");
        }
        return sb.toString();
    }
}
