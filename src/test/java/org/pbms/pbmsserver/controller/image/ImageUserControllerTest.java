package org.pbms.pbmsserver.controller.image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.common.auth.TokenBean;
import org.pbms.pbmsserver.controller.BaseControllerTest;
import org.pbms.pbmsserver.repository.enumeration.user.UserRoleEnum;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.pbms.pbmsserver.repository.model.UserInfo;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.lifecycle.upload.SaveProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageUserControllerTest extends BaseControllerTest {

    private final MockMultipartFile image = new MockMultipartFile("image",
            "test",
            MediaType.IMAGE_JPEG.toString(),
            new byte[]{
                    -1, -40, -1, -32
            }
    );
    @SpyBean
    private SaveProcessor saveProcessor;
    @Autowired
    private UserSettingsMapper userSettingsMapper;

    private UserInfo user;

    @Override
    protected TokenBean getTokenBean() {
        return new TokenBean(this.user.getUserId(), this.user.getUserName(),
                UserRoleEnum.transform(this.user.getRole()));
    }

    @BeforeEach
    private void setUp() {
        this.userSettingsMapper.delete(c -> c);
        this.userInfoMapper.delete(c -> c);

        this.user = this.insertDefaultAdmin();

        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(this.user.getUserId());
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setWatermarkLogoRepeat(false);
        userSettings.setWatermarkLogoGradient((byte) 0);
        userSettings.setWatermarkLogoAlpha((byte) 0);
        userSettings.setWatermarkTextEnable(false);
        userSettings.setWatermarkTextContent("unbelievable!");
        userSettings.setWatermarkTextAlpha((byte) 0);
        userSettings.setCompressScale((byte) 0);
        userSettings.setResponseReturnType("markdown");
        userSettingsMapper.insert(userSettings);

        doNothing().when(saveProcessor).save(any(), any(), any());
    }


    // 正常情况
    @Test
    void uploadImageByCommonToken() throws Exception {
        ArrayList<MockMultipartFile> multipartFiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            multipartFiles.add(image);
        }
        this.filePost("/image", multipartFiles).andExpect(status().isOk());
    }

    @Test
    void uploadImageByCommonToken_imageEmpty() throws Exception {
        MockMultipartFile emptyImage = new MockMultipartFile("image",
                "test",
                MediaType.IMAGE_JPEG.toString(),
                new byte[]{}
        );
        this.filePost("/image", emptyImage)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("请选择上传文件"));
    }
}
