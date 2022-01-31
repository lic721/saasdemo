package com.saas.demo.common.base.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author oliver.liu
 * @Date 2019/1/15 18:26
 */
public class Result<T> {

    @ApiModelProperty(value = "返回的状态码;200:成功,499:未知错误,500:服务器发生错误", required = true)
    private String code;

    @ApiModelProperty(value = "返回的请求的响应信息")
    private String msg;

    @ApiModelProperty(value = "响应对象")
    private T data;

    public Result() {
    }

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setResult(String code, String msg, boolean success) {
        this.code = code;
        this.msg = msg;
    }

}
