package org.pbms.pbmsserver.service.lifecycle.upload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbms.pbmsserver.common.constant.ServerConstant;
import org.pbms.pbmsserver.common.exception.ServerException;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class SaveProcessorTest {
    @Spy
    private SaveProcessor saveProcessor;
    private MockedStatic<ServerConstant> serverConstantMockedStatic;
    private MockMultipartFile mockMultipartFile;

    @BeforeEach
    public void setup() throws IOException {
        this.mockMultipartFile = new MockMultipartFile("come_on_for_test.jpg", "come_on_for_test.jpg", "image/jpeg", new byte[]{0, 1});
        serverConstantMockedStatic = mockStatic(ServerConstant.class);
        serverConstantMockedStatic.when(ServerConstant::getAbsoluteURLUploadPath)
                .thenReturn(this.getClass().getResource("/image").getPath());
        serverConstantMockedStatic.when(ServerConstant::getAbsoluteUploadPath)
                .thenReturn(this.getClass().getResource("/image").getPath());
    }

    @Test
    public void upload_with_fileName() {
        String excepted = this.getClass().getResource("/image").getPath() + "/I_am_the_king.jpg";
        String actual = this.saveProcessor.upload("I_am_the_king.jpg", mockMultipartFile);
        serverConstantMockedStatic.close();
        assertTrue(new File(excepted).exists());
        assertEquals(excepted, actual);
    }

    private static Stream<Arguments> without_names() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("")
        );
    }

    @ParameterizedTest
    @MethodSource("without_names")
    public void upload_without_fileName(String name) {
        String excepted = this.getClass().getResource("/image").getPath() + "/come_on_for_test.jpg";
        String actual = this.saveProcessor.upload(name, mockMultipartFile);
        serverConstantMockedStatic.close();
        assertTrue(new File(excepted).exists());
        assertEquals(excepted, actual);
    }
}