package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 参数为空时使用的异常，可以是<code>null</code>, <code>blank</code>, <code>empty</code>
 *   
 * @author zyl
 * @date 2021/07/12 23:12:32
 */
public class ParamNullException extends BaseException {
    public ParamNullException(HttpStatus code, String message) {
        super(code, message);
    }
}
