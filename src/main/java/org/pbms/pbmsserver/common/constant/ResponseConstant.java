package org.pbms.pbmsserver.common.constant;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 返回类型常量读取
 * 
 * @author zyl
 * @date 2021/07/10 13:25:11
 */
@Component
public class ResponseConstant {
    private static final Logger log = LoggerFactory.getLogger(ResponseConstant.class);

    private List<String> supportResponseType = Arrays.asList("markdown", "url");

    private boolean isValidResponseType(String responseType) {
        return supportResponseType.contains(responseType);
    }

    public static String RESPONSE_TYPE;

    @Value("${response.type:markdown}")
    public void setRespnseType(String responseType) {
        if (!isValidResponseType(responseType)) {
            log.warn("response.type不支持配置项:{}, 初始化为默认值markdown", responseType);
            responseType = "markdown";
        }
        ResponseConstant.RESPONSE_TYPE = responseType;
        log.info("set responseType:{}", ResponseConstant.RESPONSE_TYPE);
    }
}
