package org.pbms.pbmsserver.service.lifecycle.after;


import cn.hutool.core.io.file.FileNameUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbms.pbmsserver.repository.model.UserSettings;
import org.pbms.pbmsserver.service.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ResponseProcessorTest {

    @InjectMocks
    ResponseProcessor responseProcessor;

    @Mock
    UserService userService;

    MultipartFile image = new MockMultipartFile("test.png", "test.png", MediaType.IMAGE_PNG.getType(), new byte[]{});

    String url = "http://127.0.0.1/image";

    UserSettings getUserSettings(String type) {
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(1L);
        userSettings.setResponseReturnType(type);
        return userSettings;
    }

    @Test
    void testResponseHandler_url() {
        doReturn(this.getUserSettings("url")).when(this.userService).getSettings();
        String expected = this.url;
        String actual = responseProcessor.responseHandler(this.image, url);
        assertEquals(expected, actual);
    }

    @Test
    void testResponseHandler_markdown() {
        doReturn(this.getUserSettings("markdown")).when(this.userService).getSettings();
        String expected = "![" + FileNameUtil.mainName(this.image.getOriginalFilename()) + "](" + this.url + ")";
        String actual = responseProcessor.responseHandler(this.image, url);
        assertEquals(expected, actual);
    }
}