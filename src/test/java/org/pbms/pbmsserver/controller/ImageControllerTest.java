package org.pbms.pbmsserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.request.image.ImageUploadReq;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;
import org.pbms.pbmsserver.repository.mapper.UserSettingsMapper;
import org.pbms.pbmsserver.service.lifecycle.before.CompressProcessor;
import org.pbms.pbmsserver.service.lifecycle.before.WaterMarkProcessor;
import org.pbms.pbmsserver.service.lifecycle.upload.SaveProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

class ImageControllerTest extends BaseControllerTestWithAuth {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserSettingsMapper userSettingsMapper;
    @MockBean
    private WaterMarkProcessor waterMarkProcessor;
    @MockBean
    private CompressProcessor compressProcessor;
    @SpyBean
    private SaveProcessor saveProcessor;
    private ImageUploadReq req;
    private InputStream originInputStream;
    private MockMultipartFile mockMultipartFile;
    private MockedStatic<ServerConstant> serverConstantMockedStatic;

    @Override
    protected void setup() {
        userInfoMapper.delete(c -> c);
        originInputStream = this.getClass().getResourceAsStream("/image/come_on_for_test.jpg");
    }

    @BeforeEach
    public void afterSetup() throws IOException {
        mockMultipartFile = new MockMultipartFile("image", "come_on_for_test.jpg", "image/jpeg", originInputStream);
        when(waterMarkProcessor.addWaterMark(any())).thenReturn(mockMultipartFile);
        when(compressProcessor.compress(any())).thenReturn(mockMultipartFile);
        userSettingsMapper.delete(c -> c);
        userSettings.setCompressScale((byte) 80);
        userSettings.setResponseReturnType("markdown");
        userSettings.setWatermarkLogoEnable(false);
        userSettings.setWatermarkTextEnable(false);
        userSettingsMapper.insert(userSettings);
        req = new ImageUploadReq();
        req.setFileName("name");
        serverConstantMockedStatic = mockStatic(ServerConstant.class);
        serverConstantMockedStatic.when(ServerConstant::getAbsoluteURLUploadPath)
                        .thenReturn("upload");
        serverConstantMockedStatic.when(ServerConstant::getAbsoluteUploadPath)
                .thenReturn(this.getClass().getResource("/fonts").getPath());
    }

    @Test
    public void uploadImage_test_multi() throws Exception {
        upload("/image", null, null, null, MediaType.APPLICATION_JSON, mockMultipartFile, mockMultipartFile)
                .andExpect(status().isOk())
                .andExpect(content().string("![come_on_for_test](upload/come_on_for_test.jpg)\n![come_on_for_test](upload/come_on_for_test.jpg)\n"));
        serverConstantMockedStatic.close();
    }

    @Test
    public void uploadImage_test_null() throws Exception {
        originInputStream = this.getClass().getResourceAsStream("/image/come_on_for_test.jpg");
        mockMultipartFile = new MockMultipartFile("wrong", "come_on_for_test.jpg", "image/jpeg", originInputStream);
        upload("/image", null, null, null, MediaType.APPLICATION_JSON, mockMultipartFile)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("请选择上传文件"));
        serverConstantMockedStatic.close();
    }

    @Test
    public void uploadImage_test_empty() throws Exception {
        originInputStream = this.getClass().getResourceAsStream("/image/come_on_for_test.jpg");
        mockMultipartFile = new MockMultipartFile("image", "come_on_for_test.jpg", "image/jpeg", new byte[] {});
        upload("/image", null, null, null, MediaType.APPLICATION_JSON, mockMultipartFile)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("请选择上传文件"));
        serverConstantMockedStatic.close();
    }
}