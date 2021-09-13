package org.pbms.pbmsserver.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ClientException {
    public enum MessageEnum {
        UNAUTHORIZED("未授权"),
        AUTHORIZED_EXPIRED("授权到期");
        private String message;

        MessageEnum(String message) {
            this.message = message;
        }
    }

    public UnauthorizedException(MessageEnum messageEnum) {
        super(HttpStatus.UNAUTHORIZED, messageEnum.message);
    }

}
