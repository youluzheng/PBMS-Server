package org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadProcessor.decodeProcessor;

import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class FileNameDecoderProcessorTest {
    private static final Logger log = LoggerFactory.getLogger(FileNameDecoderProcessorTest.class);

    private MultipartFile image = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG.toString(), new byte[]{0, 1, 1, 0, 0, 0, 0, 1});

    @Test
    void process_hash() {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.buildProcessor("${hash}", this.image);
        String expected = "46BE251D34787DC9025FD369DB3C00C0";
        String actual = fileNameDecodeProcessor.process().toUpperCase();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_null() {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.buildProcessor(null, this.image);
        String expected = null;
        String actual = fileNameDecodeProcessor.process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_empty() {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.buildProcessor("", this.image);
        String expected = "";
        String actual = fileNameDecodeProcessor.process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void processor_fileName() {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.buildProcessor("${fileName}", this.image);
        String expected = "test";
        String actual = fileNameDecodeProcessor.process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void testFileNameDecodeProcessor_fullName() {
        FileNameDecodeProcessor fileNameDecodeProcessor = FileNameDecodeProcessor.buildProcessor("${fullName}", this.image);
        String expected = "test.png";
        String actual = fileNameDecodeProcessor.process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_yyyy_MM_dd_HH_mm_ss() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String expected = String.format("%02d-%02d/%02d %02d:%02d:%02d", year, month, day, hour, min, second);
        String actual = FileNameDecodeProcessor.buildProcessor("${yyyy}-${MM}/${dd} ${HH}:${mm}:${ss}", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_yyyy_MM_dd_HH_mm_ss_2() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String expected = String.format("123%02d/%02d/%02d/%02d/%02d/%02d456", year, month, day, hour, min, second);
        String actual = FileNameDecodeProcessor.buildProcessor("123${yyyy}/${MM}/${dd}/${HH}/${mm}/${ss}456", this.image).process();
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
        String actual = FileNameDecodeProcessor.buildProcessor("${yy}-${M} abc ${d} ${H}:${m}:${s}", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_$() {
        String expected = "$";
        String actual = FileNameDecodeProcessor.buildProcessor("$", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_$$() {
        String expected = "$$";
        String actual = FileNameDecodeProcessor.buildProcessor("$$", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_exception() {
        try {
            FileNameDecodeProcessor.buildProcessor("${not support}", this.image).process();
            fail("未抛出指定异常");
        } catch (BusinessException e) {
            String expected = BusinessStatus.ENCODING_NOT_SUPPORT.toString("不支持${not support}编码");

            log.debug("expected:{}, actual:{}", expected, e.getMessage());
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void process_not_close() {
        String expected = "${yyyy";
        String actual = FileNameDecodeProcessor.buildProcessor("${yyyy", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_empty_close() {
        try {
            FileNameDecodeProcessor.buildProcessor("${}", this.image).process();
            fail("未抛出指定异常");
        } catch (BusinessException e) {
            String expected = BusinessStatus.ENCODING_NOT_SUPPORT.toString("不支持${}编码");

            log.debug("expected:{}, actual:{}", expected, e.getMessage());
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void process_without_$_close() {
        String expected = "{abc}";
        String actual = FileNameDecodeProcessor.buildProcessor("{abc}", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }

    @Test
    void process_ext() {
        String expected = "png";
        String actual = FileNameDecodeProcessor.buildProcessor("${ext}", this.image).process();
        log.debug("expected:{}, actual:{}", expected, actual);
        assertEquals(expected, actual);
    }
}