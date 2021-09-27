package org.pbms.pbmsserver.common;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

public class TEXT_HTML_Converter extends AbstractHttpMessageConverter<Object> {

    public TEXT_HTML_Converter() {
        // 设置支持的转换类型，可以多个
        super(MediaType.TEXT_HTML);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage){
        throw new RuntimeException("MediaType not support");
    }

    @Override
    protected void writeInternal(Object o,
                                 HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        String out = JSONUtil.toJsonStr(o);
        outputMessage.getBody().write(out.getBytes());
    }
}