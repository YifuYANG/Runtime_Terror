package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This controller is to demonstrate the usage of authentication system
 * Join post-man work place >> https://www.getpostman.com/collections/c867cba690b7a55cda4d
 */

@Slf4j
@RestController
@RequestMapping("/example")
public class example {

    @Autowired
    private TokenPool tokenPool;

    @PostMapping("/login")
    public String loginExample(@RequestBody User userDetail) {
        /**
         * we need to check if userDetail is correct
         * Then we shouldn't allow a user logins twice
         */
        if(tokenPool.containsUserId(userDetail.getUserId())) {
            return "You can't login twice.";
        }

        /**
         * Issue a unique token to user/front-end app
         */
        String token = UUID.randomUUID().toString();
        tokenPool.login(userDetail.getUserId(), token);
        log.info(userDetail.getFirst_name() + " logged in, token issued: " + token);
        log.info("Current token pool:\n" + tokenPool.toString());
        return token;
    }

    /**
     * Always use @RestrictUserAccess annotation for restricted api
     * @param token token is stored in http header & it is supposed to be the first parameter
     * @param userDetail
     * @return
     */
    @RestrictUserAccess(requiredLevel = UserLevel.CLIENT)
    @PostMapping("/restricted-api")
    public String restrictedUserAccessExample(@RequestHeader("token") String token, @RequestBody User userDetail) {
        return "Restricted API: Hi, " + userDetail.getFirst_name();
    }

}
