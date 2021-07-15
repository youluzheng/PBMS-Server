package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源不存在
 * 
 * @author zyl
 * @date 2021/07/04 18:16:01
 */
public class ResourceNotFoundException extends ClientException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
