package com.saas.demo.common.base.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -2950950582440455800L;

    private final String code;

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
