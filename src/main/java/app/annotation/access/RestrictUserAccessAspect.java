package app.annotation.access;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RestrictUserAccessAspect {

    @Around("@annotation(app.annotation.access.RestrictUserAccess)")
    public Object restrictUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

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
