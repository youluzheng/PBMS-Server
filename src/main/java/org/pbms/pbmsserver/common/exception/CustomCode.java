package org.pbms.pbmsserver.common.exception;

/**
 * 自定义业务异常
 * 
 * @author zyl
 * @date 2021/07/04 20:34:08
 */
public enum CustomCode {

    FILE_SIZE_OUT_OF_LIMIT("00001", "文件大小超过限制"),
    FILE_TYPE_NOT_SUPPORT("00002", "文件类型不支持"),
    BLANK("10000", "占位");

    private String code;
    private String msg;

    CustomCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "{\"code\":" + this.code + ", \"message\":\"" + this.msg + "\"}";

    }

}
