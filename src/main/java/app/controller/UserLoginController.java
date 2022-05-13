package app.controller;

import app.bean.TokenPool;
import app.constant.UserLevel;
import app.dao.AttemptLimitationDao;
import app.model.Ip_logs;
import app.model.User;
import app.repository.IpAddressRepository;
import app.repository.UserRepository;
import app.vo.LoginForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    private AttemptLimitationDao attemptLimitationDao;
    @Autowired
    private IpAddressRepository ipAddressRepository;

    //login user
    //adding encoder method
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody LoginForm loginForm, HttpServletRequest request){

        Map<String, Object> map = new HashMap<>(3);
        User user = userRepository.findByUserEmail(loginForm.getUserEmail());
        Ip_logs ip_log = ipAddressRepository.findByIpAddress(request.getRemoteAddr());
        if(user == null){
            map.put("status", "fail");
            map.put("msg", "Account does not exist.");
        } else if(loginForm.getPassword().equals("")){
            map.put("status", "fail");
            map.put("msg", "Password filed should not be empty.");
        } else if (!emailValidator(loginForm.getUserEmail())){
            map.put("status", "fail");
            map.put("msg", "Wrong email type.");
        } else if(ip_log!=null && !attemptLimitationDao.unlockWhenTimeExpired(ip_log)){
            map.put("status", "fail");
            map.put("msg", "Too many request, your ip has been baned for 20 mines");
        } else {
             if(!encoder.matches(loginForm.getPassword(),user.getPassword())){
                 if(ip_log==null) {
                     ipAddressRepository.save(new Ip_logs(request.getMethod(),request.getRequestURI(),request.getRemoteAddr()));
                 } else {
                     if(!ip_log.isAccountLocked()){
                         attemptLimitationDao.resetAttemptsAfterPeriodOfTime(ip_log);
                         attemptLimitationDao.setLastAttempt(ip_log);
                     }
                     if(ip_log.getFailedAttempts()>2){
                         attemptLimitationDao.lock(ip_log);
                     } else {
                         attemptLimitationDao.increaseFailedAttempts(ip_log);
                     }
                 }
                map.put("status", "fail");
                map.put("msg", "Wrong password.");
            } else {
                String token = tokenPool.generateToken();
                tokenPool.login(user.getUserId(), token);
                log.info("Token issued to " + user.getUserId());
                if(ip_log!=null){
                    attemptLimitationDao.resetFailedAttempts(ip_log);
                }
                map.put("status", "success");
                map.put("token", token);
                map.put("role", getUserRoleByToken(token).name());
            }
        }
        return map;
    }

    @GetMapping("/sign_out")
    @ResponseBody
    public Map<String, String> logout(@RequestHeader("token") String token) {
        Map<String, String> map = new HashMap<>(3);
        if(tokenPool.containsToken(token)){
            log.info("Token removed for user id = " + tokenPool.getUserIdByToken(token));
            tokenPool.logout(token);
            map.put("status", "success");
        } else {
            log.warn("Logout failed: Invalid token -> " + token);
            map.put("status", "fail");
        }
        return map;
    }

    @GetMapping("/login")
    public String loginPage() {
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

    private UserLevel getUserRoleByToken(String token) {
        Long userId = tokenPool.getUserIdByToken(token);
        return userRepository.findById(userId).get().getUserLevel();
    }
}
