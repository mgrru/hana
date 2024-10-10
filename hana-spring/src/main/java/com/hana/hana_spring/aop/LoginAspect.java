package com.hana.hana_spring.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
@Async
public class LoginAspect {
    @Autowired
    private JwtUtil jwt_util;

    @Around("@annotation(com.hana.hana_spring.anno.LoginValidate) || @within(com.hana.hana_spring.anno.LoginValidate)")
    public Object around(ProceedingJoinPoint join_point) throws Throwable {
        // 检查是否需要验证
        Method method = ((MethodSignature) join_point.getSignature()).getMethod();
        LoginValidate login_validate = method.getAnnotation(LoginValidate.class);

        if (login_validate != null && !login_validate.value()) {
            return join_point.proceed(join_point.getArgs());
        }

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        if (jwt_util.validateToken(token)) {
            return join_point.proceed(join_point.getArgs());
        } else {
            return Result.error();
        }
    }
}
