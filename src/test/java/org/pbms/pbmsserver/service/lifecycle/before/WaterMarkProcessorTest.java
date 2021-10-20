package org.pbms.pbmsserver.service.lifecycle.before;

import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.UserService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaterMarkProcessorTest {
    @InjectMocks
    @Spy
    private WaterMarkProcessor waterMarkProcessor;
    @Mock
    private UserService userService;
    private UserSettings userSettings;
    private MockedStatic<ServerConstant> serverConstantMockedStatic;
    private MockMultipartFile mockMultipartFile;
    private InputStream originInputStream;

    @BeforeEach
    public void setup() throws IOException {
        this.userSettings = new UserSettings();
        this.userSettings.setWatermarkLogoAlpha((byte) 80);
        this.userSettings.setWatermarkLogoGradient((byte) 80);
        this.userSettings.setWatermarkTextAlpha((byte) 80);
        originInputStream = this.getClass().getResourceAsStream("/image/come_on_for_test.jpg");
        this.mockMultipartFile = new MockMultipartFile("come_on_for_test.jpg", "come_on_for_test.jpg", "image/jpeg", originInputStream);
        when(userService.getSettings()).thenReturn(this.userSettings);
        serverConstantMockedStatic = mockStatic(ServerConstant.class);
        serverConstantMockedStatic.when(ServerConstant::getAbsoluteLogoPath)
                .thenReturn(this.getClass().getResource("/image").getPath());
    }

    @AfterEach
    void tearDown() {
        serverConstantMockedStatic.close();
    }


    @Test
    public void addWaterMark_not_enable_test() throws IOException {
        this.userSettings.setWatermarkLogoEnable(false);
        MultipartFile waterMark = waterMarkProcessor.addWaterMark(mockMultipartFile);
        assertTrue(IoUtil.contentEquals(mockMultipartFile.getInputStream(), waterMark.getInputStream()));
    }

    @Test
    public void addWaterMark_logo_enable_not_repeat_test() throws IOException {
        this.userSettings.setWatermarkLogoEnable(true);
        this.userSettings.setWatermarkTextEnable(false);
        this.userSettings.setWatermarkLogoEnable(true);
        this.userSettings.setWatermarkLogoRepeat(false);
        MultipartFile waterMark = waterMarkProcessor.addWaterMark(mockMultipartFile);
        verify(waterMarkProcessor).addImgWaterMark(any());
        verify(waterMarkProcessor).drawImage(any(), any(), any());
        verify(waterMarkProcessor, never()).addTextWaterMark(any());
        verify(waterMarkProcessor, never()).drawRepeat(any(), any(), any());
    }

    @Test
    public void addWaterMark_logo_enable_repeat_test() throws IOException {
        this.userSettings.setWatermarkLogoEnable(true);
        this.userSettings.setWatermarkTextEnable(false);
        this.userSettings.setWatermarkLogoEnable(true);
        this.userSettings.setWatermarkLogoRepeat(true);
        MultipartFile waterMark = waterMarkProcessor.addWaterMark(mockMultipartFile);
        verify(waterMarkProcessor).addImgWaterMark(any());
        verify(waterMarkProcessor, never()).drawImage(any(), any(), any());
        verify(waterMarkProcessor, never()).addTextWaterMark(any());
        verify(waterMarkProcessor).drawRepeat(any(), any(), any());
    }

    @Test
    public void addWaterMark_text_enable_test() throws IOException {
        this.userSettings.setWatermarkLogoEnable(true);
        this.userSettings.setWatermarkTextEnable(true);
        this.userSettings.setWatermarkLogoEnable(true);
        this.userSettings.setWatermarkLogoRepeat(true);
        this.userSettings.setWatermarkTextContent("true");
        MultipartFile waterMark = waterMarkProcessor.addWaterMark(mockMultipartFile);
        verify(waterMarkProcessor).addImgWaterMark(any());
        verify(waterMarkProcessor, never()).drawImage(any(), any(), any());
        verify(waterMarkProcessor).addTextWaterMark(any());
        verify(waterMarkProcessor).drawRepeat(any(), any(), any());
    }
}