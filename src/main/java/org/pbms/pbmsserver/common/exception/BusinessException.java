package org.pbms.pbmsserver.common.exception;

/**
 * 客户端异常，4xx
 * <P>
 * 如果异常的原因出自于客户端，可以使用该异常或子异常
 * 
 * @author zyl
 * @date 2021/07/04 18:12:08
 */
public class BusinessException extends RuntimeException {
    private CustomCode code;

    public BusinessException(CustomCode code) {
        this.code = code;
    }

    /**
     * @return the code
     */
    public CustomCode getCode() {
        return code;
    }
}
