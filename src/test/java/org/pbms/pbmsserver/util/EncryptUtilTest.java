package org.pbms.pbmsserver.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptUtilTest {

    private static final Logger log = LoggerFactory.getLogger(EncryptUtilTest.class);

    private static Stream<String> generateInvalidValue_sha512() {
        return Stream.of(null, "", " ", "      ");
    }

    @ParameterizedTest
    @MethodSource("generateInvalidValue_sha512")
    void testSha512_invalid(String input) {
        assertThrows(ParamNullException.class, () -> EncryptUtil.sha512(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {" 1 ", "123456", "1234567891234567912345679", "asdfdasf", "+_+---213!@%^&"})
    void testSha512_success(String input) {
        assertEquals(128, EncryptUtil.sha512(input).length());
    }
}