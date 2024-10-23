package com.hana.hana_spring.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@Async
public class ValiAspect {
    @Autowired
    private JwtUtil jwt_util;

    @Pointcut("@annotation(com.hana.hana_spring.anno.Validate) || @within(com.hana.hana_spring.anno.Validate)")
    public void validate() {
    }

    @Order(1)
    @Around("validate()")
    public Object login_validate(ProceedingJoinPoint join_point) throws Throwable {
        // 检查是否需要验证
        Method method = ((MethodSignature) join_point.getSignature()).getMethod();
        Validate validate = method.getAnnotation(Validate.class);

        // 如果方法上没有注解，再尝试获取类上的 @Validate 注解
        if (validate == null) {
            Class<?> targetClass = method.getDeclaringClass();
            validate = targetClass.getAnnotation(Validate.class);
        }

        if (validate != null && !validate.login()) {
            return join_point.proceed(join_point.getArgs());
        }

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        if (jwt_util.validateToken(token)) {
            return join_point.proceed(join_point.getArgs());
        } else {
            return Result.no_auth();
        }
    }

    @Order(2)
    @Around("validate()")
    public Object admin_validate(ProceedingJoinPoint join_point) throws Throwable {
        // 检查是否需要验证
        Method method = ((MethodSignature) join_point.getSignature()).getMethod();
        Validate validate = method.getAnnotation(Validate.class);

        // 如果方法上没有注解，再尝试获取类上的 @Validate 注解
        if (validate == null) {
            Class<?> targetClass = method.getDeclaringClass();
            validate = targetClass.getAnnotation(Validate.class);
        }

        if (validate != null && !validate.auth()) {
            return join_point.proceed(join_point.getArgs());
        }

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = req.getHeader("Authorization");
        if (jwt_util.verify_admin(token)) {
            return join_point.proceed(join_point.getArgs());
        } else {
            return Result.no_auth();
        }

    }
}
