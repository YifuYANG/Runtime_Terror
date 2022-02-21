package app.controller;


import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.dao.ActivityDao;
import app.model.User;
import app.repository.UserRepository;
import app.vo.ActivityForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    TokenPool tokenPool;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityDao activityDao;

    @RestrictUserAccess(requiredLevel = UserLevel.ANY)
    @GetMapping
    public String activityPage(@RequestHeader("token") String token, Model model) {
        Long userid=tokenPool.getUserIdByToken(token);
        User user=userRepository.findByUserId(userid);
        List<ActivityForm> activities = activityDao.findAllActivitiesByUserId(userid);
        model.addAttribute("user", user.getLast_name());
        model.addAttribute("activities", activities);
        return "activity";
    }
}
