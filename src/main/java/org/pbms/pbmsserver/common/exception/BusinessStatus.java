package org.pbms.pbmsserver.common.exception;

/**
 * 自定义业务异常
 *
 * @author zyl
 * @date 2021/07/04 20:34:08
 */
public enum BusinessStatus {

    FILE_SIZE_OUT_OF_LIMIT("00001", "文件大小超过限制"),
    FILE_TYPE_NOT_SUPPORT("00002", "文件类型不支持"),
    ENCODING_NOT_SUPPORT("00003", "编码不支持"),

    /*----------用户模块 01*** ----------*/
    USERNAME_OR_PASSWORD_ERROR("01001", "用户名不存在或审核未通过或密码错误"),
    USER_NOT_FOUND("01002", "用户不存在"),
    USERNAME_ALREADY_EXISTS("01003", "用户名称重复"),
    EMAIL_REGISTERED("01004", "邮箱已注册"),
    INVALID_LINK("01005", "链接已失效"),
    WAITING_FOR_AUDIT("01006", "请等待审核"),
    EMAIL_SENT("01007", "邮件已发送，请查收邮件"),


    /*----------临时token模块 02*** ----------*/
    TEMP_TOKEN_ERROR("02001", "临时token有误，请重新输入"),
    TEMP_TOKEN_EXPIRE("02002", "临时token已过期"),
    TEMP_TOKEN_UPLOAD_TIMES_EXCEED("02003", "临时token上传次数超限"),

    BLANK("10000", "占位");

    private String code;
    private String msg;

    BusinessStatus(String code, String msg) {
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

    public String toString(String extra) {
        return "{\"code\":" + this.code + ", \"message\":\"" + this.msg + ", " + extra + "\"}";
    }

}
