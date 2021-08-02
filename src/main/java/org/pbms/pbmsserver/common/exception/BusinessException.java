package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 业务异常，如账号密码错误等 <code>HTTPStatus=403</code>
 * 
 * @author zyl
 * @date 2021/07/04 18:12:08
 */
public class BusinessException extends BaseException {

    public BusinessException(BusinessStatus businessStatus, String extra) {
        super(HttpStatus.FORBIDDEN, businessStatus.toString(extra));
    }

    public BusinessException(BusinessStatus businessStatus) {
        super(HttpStatus.FORBIDDEN, businessStatus.toString());
    }

}
