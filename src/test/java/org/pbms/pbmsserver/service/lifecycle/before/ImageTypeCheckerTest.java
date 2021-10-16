package org.pbms.pbmsserver.service.lifecycle.before;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pbms.pbmsserver.common.constant.ConfigReader;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ImageTypeCheckerTest {

    private static final Logger log = LoggerFactory.getLogger(ImageTypeCheckerTest.class);

    @Spy
    ImageTypeChecker imageTypeChecker;

    final String fileName = "test.png";

    @BeforeEach
    void setup() {
        ConfigReader.configReader.setSupportType("png, jpg");
    }

    MockMultipartFile generateFile(byte[] data) {
        return new MockMultipartFile("image", this.fileName, MediaType.IMAGE_PNG.getType(), data);
    }

    @Test
    void checkImageType_png() {
        MockMultipartFile file_png = this.generateFile(new byte[]{
                (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47,
                (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A
        });
        this.imageTypeChecker.checkImageType(file_png);
    }

    @Test
    void checkImageType_jpg() {
        MockMultipartFile file_png = this.generateFile(new byte[]{
                -1, -40, -1, -32
        });
        this.imageTypeChecker.checkImageType(file_png);
    }

    @Test
    void checkImageType_invalid() {
        MockMultipartFile file_png = this.generateFile(new byte[]{
                -11, -40, -1, -32
        });
        try {
            this.imageTypeChecker.checkImageType(file_png);
            fail("未抛出指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.FILE_TYPE_NOT_SUPPORT.toString());
        }
    }
}