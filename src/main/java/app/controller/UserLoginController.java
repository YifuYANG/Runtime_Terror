package app.controller;


import app.bean.TokenPool;
import app.model.User;
import app.repository.UserRepository;
import app.vo.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@Slf4j
@RestController
public class UserLoginController {
    @Autowired
    private TokenPool tokenPool;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String loginExample(@RequestBody LoginForm loginForm) {
        /**
         * we need to check if userDetail is correct
         * Then we shouldn't allow a user logins twice
         */
        User user = userRepository.findByUserEmail(loginForm.getUserEmail());
        System.out.println(loginForm.getUserEmail());
        if(user == null){
            return "Account does not exist.";
        } if(tokenPool.containsUserId(user.getUserId())) {
            return "You can't login twice.";
        } else if(!loginForm.getPassword().equals(user.getPassword())){
            return "Wrong username or password.";
        } else {
            /**
             * Issue a unique token to user/front-end app
             */
            String token = UUID.randomUUID().toString();
            tokenPool.login(user.getUserId(), token);
            return token;
        }
    }

    @PostMapping("/logout")
    public String logoutExample(@RequestHeader("token") String token) {
        long userId = tokenPool.getUserIdByToken(token);
        tokenPool.logout(token);
        return userId + " logged out successfully";
    }
}
