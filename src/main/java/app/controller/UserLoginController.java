package app.controller;

import app.bean.TokenPool;
import app.model.User;
import app.repository.UserRepository;
import app.vo.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class UserLoginController {
    @Autowired
    private TokenPool tokenPool;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    //login user
    //adding encoder method
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody LoginForm loginForm) {
        Map<String, Object> map = new HashMap<>(3);
        User user = userRepository.findByUserEmail(loginForm.getUserEmail());
        if (!emailValidator(loginForm.getUserEmail())){
            map.put("status", "fail");
            map.put("msg", "Wrong email type, dont hack me?!?!");
        } else if(user == null){
            map.put("status", "fail");
            map.put("msg", "Account does not exist.");
        } else if(!encoder.matches(loginForm.getPassword(),user.getPassword())){
            map.put("status", "fail");
            map.put("msg", "Wrong username or password.");
        } else if(tokenPool.containsUserId(user.getUserId())) {
            map.put("status", "fail");
            map.put("msg", "You can not login twice");
        } else {
            String token = tokenPool.generateToken();
            tokenPool.login(user.getUserId(), token);
            map.put("status", "success");
            map.put("token", token);
        }
        return map;
    }

    @PostMapping("/logout")
    @ResponseBody
    public String logout(@RequestHeader("token") String token) {
        long userId = tokenPool.getUserIdByToken(token);
        tokenPool.logout(token);
        return userId + " logged out successfully";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    //validate email type at back end in case of attack may bypass front side
    private Boolean emailValidator(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Boolean passwordValidator(String password){
        return true;
    }
}
