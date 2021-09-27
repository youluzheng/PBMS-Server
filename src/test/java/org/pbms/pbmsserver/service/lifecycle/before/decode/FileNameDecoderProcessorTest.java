package org.pbms.pbmsserver.service.lifecycle.before.decode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FileNameDecoderProcessorTest {
    private static final Logger log = LoggerFactory.getLogger(FileNameDecoderProcessorTest.class);

    private final MultipartFile image = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG.toString(), new byte[]{0, 1});

    @Test
    void process_hash() {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.of("${hash}", this.image);
        assertEquals("80536c6170dd8626dc081af148d39ec2", fileNameDecodeProcessor.process());
    }

    private static Stream<Arguments> generateValue_process_file() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("${fileName}", "test"),
                Arguments.of("${fullName}", "test.png"),
                Arguments.of("$", "$"),
                Arguments.of("$$", "$$"),
                Arguments.of("${yyyy", "${yyyy"),
                Arguments.of("{abc}", "{abc}"),
                Arguments.of("${ext}", "null")
        );
    }

    @ParameterizedTest
    @MethodSource("generateValue_process_file")
    void process_file(String input, String expected) {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.of(input, this.image);
        String actual = fileNameDecodeProcessor.process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> generateValue_process_date() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return Stream.of(
                Arguments.of("${yyyy}-${MM}/${dd} ${HH}:${mm}:${ss}",
                        String.format("%02d-%02d/%02d %02d:%02d:%02d", year, month, day, hour, min, second)),
                Arguments.of("123${yyyy}/${MM}/${dd}/${HH}/${mm}/${ss}456",
                        String.format("123%02d/%02d/%02d/%02d/%02d/%02d456", year, month, day, hour, min, second))
        );
    }

    @ParameterizedTest
    @MethodSource("generateValue_process_date")
    void process_yyyy_MM_dd_HH_mm_ss(String input, String expected) {
        String actual = FileNameDecodeProcessor.of(input, this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_yy_M_d_H_m_s() {
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR)).substring(2);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String expected = year + String.format("-%d abc %d %d:%d:%d", month, day, hour, min, second);
        String actual = FileNameDecodeProcessor.of("${yy}-${M} abc ${d} ${H}:${m}:${s}", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> generateValue_process_exception() {
        return Stream.of(
                Arguments.of("${not support}",
                        BusinessStatus.ENCODING_NOT_SUPPORT.toString("不支持${not support}编码")),
                Arguments.of("${}",
                        BusinessStatus.ENCODING_NOT_SUPPORT.toString("不支持${}编码"))
        );
    }

    @ParameterizedTest
    @MethodSource("generateValue_process_exception")
    void process_exception(String input, String expected) {
        FileNameDecodeProcessor processor = FileNameDecodeProcessor.of(input, this.image);
        try {
            processor.process();
            fail("未抛出指定异常");
        } catch (BusinessException e) {
            log.debug("expected:{}, actual:{}", expected, e.getMessage());
            assertEquals(expected, e.getMessage());
        }
    }
}