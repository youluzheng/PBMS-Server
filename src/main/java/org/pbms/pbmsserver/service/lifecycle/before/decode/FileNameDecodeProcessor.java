package org.pbms.pbmsserver.service.lifecycle.before.decode;

import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.util.EncryptUtil;
import org.pbms.pbmsserver.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileNameDecodeProcessor implements DecodeProcessor {
    private static final Logger log = LoggerFactory.getLogger(FileNameDecodeProcessor.class);
    private Calendar cal = Calendar.getInstance();
    private final Map<String, Decoder> decoderMap = new HashMap<>();

    private String fileName;
    private MultipartFile image;
    private static final FileNameDecodeProcessor fileNameDecoderProcessor = new FileNameDecodeProcessor();

    @Override
    public String decode(String pattern) {
        if (this.decoderMap.containsKey(pattern)) {
            return this.decoderMap.get(pattern).decode();
        }
        throw new BusinessException(BusinessStatus.ENCODING_NOT_SUPPORT, "不支持" + pattern + "编码");
    }

    @Override
    public void registerDecoder(String pattern, Decoder decoder) {
        this.decoderMap.put(pattern, decoder);
    }

    private FileNameDecodeProcessor() {
        this.registerDecoder("${yyyy}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.YEAR));
        });
        this.registerDecoder("${yy}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.YEAR)).substring(2);
        });
        this.registerDecoder("${MM}", () -> {
            this.cal.setTime(new Date());
            return String.format("%02d", this.cal.get(Calendar.MONTH) + 1);
        });
        this.registerDecoder("${M}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.MONTH) + 1);
        });
        this.registerDecoder("${dd}", () -> {
            this.cal.setTime(new Date());
            return String.format("%02d", this.cal.get(Calendar.DAY_OF_MONTH));
        });
        this.registerDecoder("${d}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.DAY_OF_MONTH));
        });
        this.registerDecoder("${HH}", () -> {
            this.cal.setTime(new Date());
            return String.format("%02d", this.cal.get(Calendar.HOUR_OF_DAY));
        });
        this.registerDecoder("${H}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.HOUR_OF_DAY));
        });
        this.registerDecoder("${mm}", () -> {
            this.cal.setTime(new Date());
            return String.format("%02d", this.cal.get(Calendar.MINUTE));
        });
        this.registerDecoder("${m}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.MINUTE));
        });
        this.registerDecoder("${ss}", () -> {
            this.cal.setTime(new Date());
            return String.format("%02d", this.cal.get(Calendar.SECOND));
        });
        this.registerDecoder("${s}", () -> {
            this.cal.setTime(new Date());
            return String.valueOf(this.cal.get(Calendar.SECOND));
        });
        this.registerDecoder("${hash}", () -> EncryptUtil.sha512(this.fileName).substring(0, 32));
        this.registerDecoder("${fileName}", () -> FileUtil.getFileNameWithoutExt(this.image));
        this.registerDecoder("${fullName}", () -> this.image.getOriginalFilename());
        this.registerDecoder("${ext}", () -> FileUtil.getFileExt(this.image));
    }

    public static FileNameDecodeProcessor of(String fileName, MultipartFile image) {
        log.debug("fileName:{}", fileName);
        FileNameDecodeProcessor.fileNameDecoderProcessor.fileName = fileName;
        FileNameDecodeProcessor.fileNameDecoderProcessor.image = image;
        return FileNameDecodeProcessor.fileNameDecoderProcessor;
    }

    @Override
    public String process() {
        if (fileName == null || fileName.isBlank()) {
            return fileName;
        }
        return this.scan(fileName);
    }
}
