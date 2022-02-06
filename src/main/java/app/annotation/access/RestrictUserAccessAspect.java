package app.annotation.access;

import app.bean.TokenPool;
import app.constant.UserLevel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

@Aspect
public class RestrictUserAccessAspect {

    @Autowired
    TokenPool tokenPool;

    @Around("@annotation(app.annotation.access.RestrictUserAccess)")
    public Object restrictUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserLevel level = method.getAnnotation(RestrictUserAccess.class).requiredLevel();
        Object args[] = joinPoint.getArgs();

        try {
            /**
             * Implement authentication logic here
             */
            return joinPoint.proceed();
        } catch (Exception e) {
            return "Access denied.";
        }

    }

}
