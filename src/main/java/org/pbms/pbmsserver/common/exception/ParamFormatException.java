package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 参数格式不符合要求时的异常，如日期格式等
 * 
 * @author zyl
 * @date 2021/07/12 23:10:18
 */
public class ParamFormatException extends BaseException{
    public ParamFormatException(HttpStatus code, String message) {
        super(code, message);
    }
}
