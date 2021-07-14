package org.pbms.pbmsserver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.pbms.pbmsserver.common.exception.ParamFormatException;
import org.pbms.pbmsserver.common.exception.ParamNullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtilTest {
    private static final Logger log = LoggerFactory.getLogger(FileUtilTest.class);

    @ParameterizedTest
    @NullSource
    void testGetFileNameWithoutExt_string_null(String input) {
        assertThrows(NullPointerException.class, () -> FileUtil.getFileNameWithoutExt(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "       "})
    void testGetFileNameWithoutExt_string_ParamNullException(String input) {
        assertThrows(ParamNullException.class, () -> FileUtil.getFileNameWithoutExt(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"\\", "a/", "a.", "!adf~", "asdf@"})
    void testGetFileNameWithoutExt_string_ParamFormatException(String input) {
        assertThrows(ParamFormatException.class, () -> FileUtil.getFileNameWithoutExt(input));
    }

    private static Stream<Arguments> provideGetFileNameWithoutExt_correctInput() {
        return Stream.of(
            Arguments.of("abc.png", "abc"),
            Arguments.of("abc", "abc"),
            Arguments.of("a/b", "b"),
            Arguments.of("a/b.c", "b"),
            Arguments.of("a/b/c.d", "c"),
            Arguments.of("a/b/~c.d.e", "~c")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetFileNameWithoutExt_correctInput")
    void testGetFileNameWithoutExt_string_correctInput(String input, String expected) {
        String actual = FileUtil.getFileNameWithoutExt(input);
        assertEquals(expected, actual);
    }

    @Test
    void testGetFileNameWithoutExt_file_null() {
        File file = null;
        assertThrows(NullPointerException.class, () -> FileUtil.getFileNameWithoutExt(file));
    }

    @Test
    void testGetFileNameWithoutExt_file_correctInput() {
        File file = new File("./test.png");
        String actual = FileUtil.getFileNameWithoutExt(file);
        String expected = "test";
        assertEquals(expected, actual);
    }

    @Test
    void testGetFileNameWithoutExt_multipartfile_null() {
        MultipartFile file = null;
        assertThrows(NullPointerException.class, () -> FileUtil.getFileNameWithoutExt(file));
    }

    @Test
    void testGetFileExt_String_null() {
        String input = null;
        assertThrows(NullPointerException.class, () -> FileUtil.getFileExt(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc/abc", "abc"})
    void testGetFileExt_String_ParamFormatException(String input) {
        assertThrows(ParamFormatException.class, () -> FileUtil.getFileExt(input));
    }

    private static Stream<Arguments> provideGetFileExt_correctInput() {
        return Stream.of(
            Arguments.of("abc.png", "png"),
            Arguments.of("abc.gif", "gif"),
            Arguments.of("a/c.c", "c"),
            Arguments.of("a/~b.c", "c"),
            Arguments.of("a/b/c.d.c", "d.c"),
            Arguments.of("a/b/~c.d.e.f", "d.e.f")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetFileExt_correctInput")
    void testGetFileExt_String_corrcetInput(String input, String expected) {
        assertEquals(expected, FileUtil.getFileExt(input));
    }

    @Test
    void testGetFileExt_file_null() {
        File file = null;
        assertThrows(NullPointerException.class, () -> FileUtil.getFileExt(file));
    }

    @Test
    void testGetFileExt_file_correctInput() {
        File file = new File("test.png");
        String actual = FileUtil.getFileExt(file);
        String expected = "png";
        assertEquals(expected, actual);
    }
}
