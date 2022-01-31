package com.saas.demo.common.base.exception;

import com.saas.demo.common.base.response.ResultCode;
import com.saas.demo.common.base.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BaseExceptionHandler {

    @Autowired
    private HttpServletRequest request;

    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public Result defaultErrorHandler(Throwable e) {
        Result result = new Result();
        result.setCode(ResultCode.INTERNAL_SERVER_ERROR.val());
        result.setMsg(ResultCode.INTERNAL_SERVER_ERROR.msg());
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        if (e instanceof BaseException) {
            Result result = new Result();
            result.setCode(((BaseException) e).getCode());
            result.setMsg(e.getMessage());
            return result;
        }
        return defaultErrorHandler(e);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Invalid Request:");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage()).append(", ");
        }
        errorMessage.delete(errorMessage.length() - 3, errorMessage.length() - 1);
        Result result = new Result();
        result.setCode(ResultCode.UNPROCESSABLE_ENTITY.val());
        result.setMsg(errorMessage.toString());
        return result;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Result result = new Result();
        result.setCode(ResultCode.BAD_REQUEST.val());
        result.setMsg(ResultCode.BAD_REQUEST.msg());
        return result;
    }

}
