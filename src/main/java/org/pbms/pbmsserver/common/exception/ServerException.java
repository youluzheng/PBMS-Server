package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 服务端异常, 5xx
 * <p>
 * 通常由于服务器错误造成的可以使用该异常或子异常
 * <p>
 * 如IOException等
 * 
 * @author zyl
 * @date 2021/07/04 18:08:34
 */
public class ServerException extends BaseException {

    public ServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ServerException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
