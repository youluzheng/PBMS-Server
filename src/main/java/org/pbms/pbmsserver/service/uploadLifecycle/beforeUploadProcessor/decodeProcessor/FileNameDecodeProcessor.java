package org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadProcessor.decodeProcessor;

import org.pbms.pbmsserver.common.exception.ServerException;
import org.pbms.pbmsserver.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileNameDecodeProcessor extends AbstractDecodeProcessor {
    private static final Logger log = LoggerFactory.getLogger(FileNameDecodeProcessor.class);
    private String fileName;
    private MultipartFile image;
    private static final FileNameDecodeProcessor fileNameDecoderProcessor = new FileNameDecodeProcessor();

    private FileNameDecodeProcessor() {
        this.registerDecoder("${hash}",
                () -> {
                    // md5
                    MessageDigest md;
                    try {
                        md = MessageDigest.getInstance("MD5");
                        md.update(this.image.getBytes());
                    } catch (NoSuchAlgorithmException e) {
                        log.error("加密方法未找到");
                        throw new ServerException();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        throw new ServerException();
                    }
                    StringBuilder buf = new StringBuilder();
                    byte[] bits = md.digest();
                    for (int bit : bits) {
                        int a = bit;
                        if (a < 0) a += 256;
                        if (a < 16) buf.append("0");
                        buf.append(Integer.toHexString(a));
                    }
                    return buf.toString();
                });
        this.registerDecoder("${fileName}", () -> FileUtil.getFileNameWithoutExt(this.image));
        this.registerDecoder("${fullName}", () -> this.image.getOriginalFilename());
        this.registerDecoder("${ext}", () -> FileUtil.getFileExt(this.image));
    }

    public static FileNameDecodeProcessor buildProcessor(String fileName, MultipartFile image) {
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
