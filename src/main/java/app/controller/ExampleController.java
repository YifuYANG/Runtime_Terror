package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * This controller is to demonstrate the usage of authentication system
 * Join post-man workplace >> https://www.getpostman.com/collections/c867cba690b7a55cda4d
 */

@Slf4j
@RestController
@RequestMapping("/example")
public class ExampleController {

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
         * Issue a unique token to user/front-end
         */
        String token = tokenPool.generateToken();
        tokenPool.login(userDetail.getUserId(), token);
        log.info(userDetail.getFirst_name() + " logged in, token issued: " + token);
        log.info("Current token pool:\n" + tokenPool.toString());
        return token;
    }

    @PostMapping("/logout")
    public String logoutExample(@RequestHeader("token") String token) {
        long userId = tokenPool.getUserIdByToken(token);
        tokenPool.logout(token);
        return userId + " logged out!";
    }

    /**
     * Always use @RestrictUserAccess annotation for restricted api
     * @param token token is stored in http header & it is supposed to be the first parameter
     * @param userDetail
     * @return
     */
    @RestrictUserAccess(requiredLevel = UserLevel.ANY)
    @PostMapping("/restricted-api")
    public String restrictedUserAccessExample(@RequestHeader("token") String token, @RequestBody User userDetail) {
        return "Restricted API: Hi, " + userDetail.getFirst_name();
    }

}
