package org.pbms.pbmsserver.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptUtilTest {

    private static final Logger log = LoggerFactory.getLogger(EncryptUtilTest.class);

    private static Stream<String> generateInvalidValue_sha512_string() {
        return Stream.of(null, "", " ", "      ");
    }

    @ParameterizedTest
    @MethodSource("generateInvalidValue_sha512_string")
    void testSha512_string_invalid(String input) {
        assertThrows(ParamNullException.class, () -> EncryptUtil.sha512(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {" 1 ", "123456", "1234567891234567912345679", "asdfdasf", "+_+---213!@%^&"})
    void testSha512_string_success(String input) {
        assertEquals(128, EncryptUtil.sha512(input).length());
    }

    @Test
    void testSha512_multipartFile_invalid() {
        MultipartFile file1 = null;
        assertThrows(ParamNullException.class, () -> EncryptUtil.sha512(file1));
        MultipartFile file2 = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG.toString(), new byte[0]);
        assertThrows(ParamNullException.class, () -> EncryptUtil.sha512(file2));
    }

    @Test
    void testSha512_multipartFile_success() {
        MultipartFile file = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG.toString(), new byte[]{0, 1});
        assertEquals("80536c6170dd8626dc081af148d39ec2", EncryptUtil.sha512(file).substring(0, 32));
    }
}