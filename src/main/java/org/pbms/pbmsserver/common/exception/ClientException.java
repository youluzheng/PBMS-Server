package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 客户端异常，区别于{@linkplain org.pbms.pbmsserver.common.exception.BusinessException
 * BusinessException}, 主要用于接口调用错误，如参数不符合规范等情况
 * 
 * @author zyl
 * @date 2021/07/15 22:08:25
 */
public class ClientException extends BaseException {

    public ClientException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 如果要返回403，使用{@linkplain org.pbms.pbmsserver.common.exception.BusinessException
     * BusinessException}
     * 
     * @author zyl
     * @date 2021/07/16 13:41:21
     */
    public ClientException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
