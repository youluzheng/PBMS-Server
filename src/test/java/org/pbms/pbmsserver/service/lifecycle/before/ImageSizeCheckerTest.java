package org.pbms.pbmsserver.service.lifecycle.before;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
class ImageSizeCheckerTest {

    private static final Logger log = LoggerFactory.getLogger(ImageSizeChecker.class);

    @Spy
    ImageSizeChecker imageSizeChecker;

    final String fileName = "test.png";
    
    @BeforeEach
    void setup() {
        ConfigReader.configReader.setMaxSize("1KB");
    }


    MockMultipartFile generateFile(byte[] data) {
        return new MockMultipartFile("image", this.fileName, MediaType.IMAGE_PNG.getType(), data);
    }

    @ParameterizedTest
    @ValueSource(ints = {1025, 10250})
    void checkImageSize_size_too_large(int size) {
        MockMultipartFile file = this.generateFile(new byte[size]);
        try {
            this.imageSizeChecker.checkImageSize(file);
            fail("未抛出指定异常");
        } catch (BusinessException e) {
            assertEquals(e.getMessage(), BusinessStatus.FILE_SIZE_OUT_OF_LIMIT.toString());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 1024})
    void checkImageSize_success(int size) {
        MockMultipartFile file = this.generateFile(new byte[size]);
        this.imageSizeChecker.checkImageSize(file);
    }
}