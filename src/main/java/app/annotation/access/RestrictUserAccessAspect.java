package app.annotation.access;

import app.bean.TokenPool;
import app.constant.UserLevel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class RestrictUserAccessAspect {

    @Autowired
    TokenPool tokenPool;

    @Around("@annotation(app.annotation.access.RestrictUserAccess)")
    public Object restrictUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserLevel requiredLevel = method.getAnnotation(RestrictUserAccess.class).requiredLevel();
        String token = joinPoint.getArgs()[0].toString();
        try {
            if(!tokenPool.containsToken(token)) throw new Exception("Access denied, please login first.");
            return joinPoint.proceed();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

}
