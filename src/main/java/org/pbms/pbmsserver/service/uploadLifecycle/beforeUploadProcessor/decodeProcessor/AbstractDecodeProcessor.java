package org.pbms.pbmsserver.service.uploadLifecycle.beforeUploadProcessor.decodeProcessor;

import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDecodeProcessor implements DecodeProcessor {
    private Calendar cal = Calendar.getInstance();
    private static final Logger log = LoggerFactory.getLogger(AbstractDecodeProcessor.class);

    private final Map<String, Decoder> decoderMap = new HashMap<>();

    public AbstractDecodeProcessor() {
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
    }

    @Override
    public String decode(String pattern) {
        if (this.decoderMap.containsKey(pattern)) {
            return this.decoderMap.get(pattern).decode();
        }
        throw new BusinessException(BusinessStatus.ENCODING_NOT_SUPPORT, "不支持" + pattern + "编码");
    }

    public void registerDecoder(String pattern, Decoder decoder) {
        this.decoderMap.put(pattern, decoder);
    }
}
