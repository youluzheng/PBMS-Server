package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 基础异常类型
 * 
 * @author zyl
 * @date 2021/07/04 17:54:38
 */
public abstract class BaseException extends RuntimeException {
    private HttpStatus httpStatus;

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
