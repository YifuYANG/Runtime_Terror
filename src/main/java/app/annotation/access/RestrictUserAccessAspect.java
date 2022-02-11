package app.annotation.access;

import app.bean.TokenPool;
import app.constant.UserLevel;
import app.repository.UserRepository;
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
    private TokenPool tokenPool;

    @Autowired
    private UserRepository userRepository;

    @Around("@annotation(app.annotation.access.RestrictUserAccess)")
    public Object restrictUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserLevel requiredLevel = method.getAnnotation(RestrictUserAccess.class).requiredLevel();
        try {
            String token = (String) joinPoint.getArgs()[0];
            token = token.replaceAll("\"", "");
            if(token == null || token.length() == 0) throw new Exception("Access denied, please login first.");
            if(!tokenPool.containsToken(token)) throw new Exception("Access denied, invalid token >> " + token);
            //If token is valid, we need to check whether user level satisfies required level
            Long userId = tokenPool.getUserIdByToken(token);
            if(requiredLevel == UserLevel.ANY)
                return joinPoint.proceed();
            if(userRepository.findById(userId).get().getUserLevel() != requiredLevel)
                throw new Exception("Access denied, you have no privileges to access this content.");
            return joinPoint.proceed();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return e.getMessage();
        }

    }

}
