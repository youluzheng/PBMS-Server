package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 参数不允许时使用的异常，如需要文件却提供了目录，主要针对某个类型中有很多子类型的情况
 * 
 * @author zyl
 * @date 2021/07/12 23:11:09
 */
public class ParamNotSupportException extends BaseException {
    public ParamNotSupportException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
