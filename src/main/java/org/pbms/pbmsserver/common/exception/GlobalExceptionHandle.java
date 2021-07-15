package org.pbms.pbmsserver.common.exception;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    private String generateJsonString(Object obj) {
        StringBuilder sb = new StringBuilder("{\"message\":\"");
        return sb.append(obj.toString()).append("\"}").toString();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String constraintViolationExceptionHandle(ConstraintViolationException e) {
        return this.generateJsonString(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        return this.generateJsonString(e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> baseExceptionHandle(BaseException e) {
        if (e.getHttpStatus().is5xxServerError()) {
            return ResponseEntity.status(e.getHttpStatus()).body("服务器异常");
        }
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    /**
     * 其他未知异常， <code>HTTPStatus=500</code>, 返回服务器异常
     * 
     * @param e 未知Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandle(Exception e) {
        log.error(e.getMessage(), e);
        return "服务器异常";
    }

}
