package com.saas.demo.common.base.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -2950950582440455800L;

    private final String code;

    public static final String NO_RECORD = "未查询到任何记录";
    public static final String INVALID_PARAMS = "参数非法";
    public static final String SYS_ERROR = "系统错误，请联系管理员";
    public static final String IMG_UPLOAD_ERROR = "图片上传异常";

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
