package org.pbms.pbmsserver.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.pbms.pbmsserver.common.exception.ParamFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.TimeZone;

public final class JSONUtil {
    private static final Logger log = LoggerFactory.getLogger(JSONUtil.class);
    private static final JsonMapper mapper = new JsonMapper();

    private JSONUtil() {
    }

    static {
        // write多余字段忽略
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略getter，setter
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // 空字段忽略
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 设置时区
        mapper.setTimeZone(TimeZone.getDefault());
    }

    /**
     * 把json字符串转换为指定的类对象
     * <p>
     * 不存在的字段会被设置<code>null</code>
     * <p>
     * 多余字段会被忽略
     *
     * @param jsonStr 待转换json字符串
     * @param clazz   目标类
     * @return 目标类实例
     * @throws NullPointerException jsonStr == null, jsonStr is blank, clazz == null
     * @throws ParamFormatException jsonStr不是有效的json格式字符串
     */
    public static <T> T str2Object(String jsonStr, Class<T> clazz) {
        Objects.requireNonNull(jsonStr);
        Objects.requireNonNull(clazz);
        if (jsonStr.isBlank()) throw new NullPointerException();
        try {
            return JSONUtil.mapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new ParamFormatException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 把实例转换为json字符串
     *
     * @param obj 待转换实例
     * @return 转换后的json字符串, "{}" 如果 obj == null
     */
    public static String object2Str(Object obj) {
        if (obj == null) return "{}";
        try {
            return JSONUtil.mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.debug("obj:{}", obj.getClass().getName());
            throw new ParamFormatException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
