package app.annotation.access;

import app.bean.TokenPool;
import app.constant.UserLevel;
import app.exception.CustomErrorException;
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
            if(token == null || token.length() == 0) {
                log.warn("Absent token detected");
                throw new CustomErrorException("Access denied, please login first.");
            }
            if(!tokenPool.containsToken(token)) {
                log.warn("Invalid token detected -> " + token);
                throw new CustomErrorException("Access denied, invalid token >> " + token);
            }
            //If token is valid, we need to check whether user level satisfies required level
            Long userId = tokenPool.getUserIdByToken(token);
            //Then check if the token is expired
            if(!tokenPool.validateTokenExpiry(token, LocalDateTime.now())) {
                log.warn("Expired token detected -> " + token);
                throw new CustomErrorException("Access denied, your token has been expired, please re-login.");
            }
            if(requiredLevel == UserLevel.ANY) {
                log.info("Token approved to execute " + method.getName());
                return joinPoint.proceed();
            }
            else if(userRepository.findById(userId).get().getUserLevel() != requiredLevel) {
                log.warn("Insufficient authorisation detected -> " + token);
                throw new CustomErrorException("Access denied, you have no privileges to access this content.");
            }
            log.info("Token approved to execute " + method.getName());
            return joinPoint.proceed();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }

    }

}
