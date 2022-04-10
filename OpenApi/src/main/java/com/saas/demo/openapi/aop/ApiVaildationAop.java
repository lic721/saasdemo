package com.saas.demo.openapi.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
@Order(1)
public class ApiVaildationAop {

    Logger log = LoggerFactory.getLogger(ApiVaildationAop.class);

    @Around("execution(public * com.saas.demo.openapi.controller..*Controller.*(..))")
    public Object doMonitor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] params = pjp.getArgs();
        log.info("请求接口:{}.{},参数:{}", method.getDeclaringClass().getSimpleName(), method.getName(), JSON.toJSONString(params));
        return proceed(pjp, method.getReturnType());
    }

    private Object proceed(ProceedingJoinPoint pjp, Class returnType) {

        Object object = null;
        try {
            object = pjp.proceed();

        } catch (Throwable throwable) {
            log.error("AOP接口调用异常", throwable);
        }
        log.info("响应接口:{}", JSON.toJSONString(object));
        return object;
    }
}
