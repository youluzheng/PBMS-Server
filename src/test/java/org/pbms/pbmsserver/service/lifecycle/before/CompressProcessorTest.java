package org.pbms.pbmsserver.service.lifecycle.before;

import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.user.UserService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompressProcessorTest {
    @InjectMocks
    private CompressProcessor compressProcessor;
    @Mock
    private UserService userService;
    private UserSettings userSettings;
    private MockedStatic<ServerConstant> serverConstantMockedStatic;
    private MockMultipartFile mockMultipartFile;
    private InputStream originInputStream;

    @BeforeEach
    public void setup() throws IOException {
        originInputStream = this.getClass().getResourceAsStream("/image/come_on_for_test.jpg");
        this.mockMultipartFile = new MockMultipartFile("come_on_for_test.jpg", "come_on_for_test.jpg", "image/jpeg", originInputStream);
        this.userSettings = new UserSettings();
        when(userService.getSettings()).thenReturn(this.userSettings);
        serverConstantMockedStatic = mockStatic(ServerConstant.class);
        serverConstantMockedStatic.when(ServerConstant::getAbsoluteTempPath)
                .thenReturn(this.getClass().getResource("/fonts").getPath());
    }

    @AfterEach
    void tearDown() {
        serverConstantMockedStatic.close();
    }

    @Test
    public void compress_scale_normal() throws IOException {
        this.userSettings.setCompressScale((byte) 80);
        MultipartFile compress = compressProcessor.compress(mockMultipartFile);
        InputStream compressInputStream = compress.getInputStream();
        assertTrue(IoUtil.contentEquals(compressInputStream, this.getClass().getResourceAsStream("/image/come_on_compress_for_test.jpg")));
    }

    @Test
    public void compress_scale_zero() throws IOException {
        this.userSettings.setCompressScale((byte) 0);
        MultipartFile compress = compressProcessor.compress(mockMultipartFile);
        InputStream compressInputStream = compress.getInputStream();
        assertTrue(IoUtil.contentEquals(compressInputStream, mockMultipartFile.getInputStream()));
    }
}