package app.controller;


import app.bean.TokenPool;
import app.model.User;
import app.repository.UserRepository;
import app.vo.LoginForm;
import com.oracle.tools.packager.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class UserLoginController {
    @Autowired
    private TokenPool tokenPool;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Map<String, Object> loginExample(@RequestBody LoginForm loginForm) {
        Map<String, Object> map = new HashMap<>(3);
        User user = userRepository.findByUserEmail(loginForm.getUserEmail());
        log.info(loginForm.getUserEmail());
        if(user == null){
            map.put("status", "fail");
            map.put("msg", "Account does not exist.");
        } else if(!loginForm.getPassword().equals(user.getPassword())){
            map.put("status", "fail");
            map.put("msg", "Wrong username or password.");
        } else if(tokenPool.containsUserId(user.getUserId())) {
            map.put("status", "fail");
            map.put("msg", "You can not login twice");
        } else {
            String token = UUID.randomUUID().toString();
            tokenPool.login(user.getUserId(), token);
            map.put("status", "success");
            map.put("token", token);
        }
        return map;
    }

    @PostMapping("/logout")
    public String logoutExample(@RequestHeader("token") String token) {
        long userId = tokenPool.getUserIdByToken(token);
        tokenPool.logout(token);
        return userId + " logged out successfully";
    }
}
