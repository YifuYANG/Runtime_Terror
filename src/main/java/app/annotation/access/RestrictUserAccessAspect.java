package app.annotation.access;

import app.bean.TokenPool;
import app.constant.UserLevel;
import app.exception.AuthenticationException;
import app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class RestrictUserAccessAspect {

    @Autowired
    private TokenPool tokenPool;

    @Autowired
    private UserRepository userRepository;

    @Pointcut("@annotation(app.annotation.access.RestrictUserAccess)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object restrictUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserLevel requiredLevel = method.getAnnotation(RestrictUserAccess.class).requiredLevel();
        try {
            String token = (String) joinPoint.getArgs()[0];
            token = token.replaceAll("\"", "");
            if(token == null || token.length() == 0) throw new AuthenticationException("Access denied, please login first.");
            if(!tokenPool.containsToken(token)) throw new AuthenticationException("Access denied, invalid token >> " + token);
            //If token is valid, we need to check whether user level satisfies required level
            Long userId = tokenPool.getUserIdByToken(token);
            //Then check if the token is expired
            if(!tokenPool.validateTokenExpiry(token, LocalDateTime.now()))
                throw new AuthenticationException("Access denied, your token has been expired, please re-login.");
            if(requiredLevel == UserLevel.ANY)
                return joinPoint.proceed();
            if(userRepository.findById(userId).get().getUserLevel() != requiredLevel)
                throw new AuthenticationException("Access denied, you have no privileges to access this content.");
            return joinPoint.proceed();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }

    }

}
