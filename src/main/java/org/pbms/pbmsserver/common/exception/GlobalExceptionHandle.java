package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * 
 * @author zyl
 * @date 2021/07/04 20:34:23
 */
@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String businessException(BusinessException e) {
        return e.getCode().toString();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> BaseException(BaseException e) {
        return ResponseEntity.status(e.getCode()).body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String runtimeException(RuntimeException e) {
        return "服务器异常";
    }

}
