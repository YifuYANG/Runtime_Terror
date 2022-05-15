package app.controller;


import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.dao.ActivityDao;
import app.exception.CustomErrorException;
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
    public String activityPage(@RequestHeader("token") String token, Model model) throws CustomErrorException {
        try {
            Long userid = tokenPool.getUserIdByToken(token);
            String userName = userRepository.findByUserId(userid).getFirst_name();
            List<ActivityForm> activities;
            if(!userIsAdmin(userid))
                activities = activityDao.findAllActivitiesByUserId(userid);
            else
                activities = activityDao.findAllActivities();
            model.addAttribute("user", userName);
            model.addAttribute("activities", activities);
            log.info("Activity page accessed, operator ID = " + tokenPool.getUserIdByToken(token));
            return "activity";
        } catch (Exception e){
            throw new CustomErrorException("some error happened");
        }
    }

    private boolean userIsAdmin(Long userId) throws CustomErrorException {
        try {
            return userRepository.findById(userId).get().getUserLevel() == UserLevel.ADMIN;
        } catch (Exception e){
            throw new CustomErrorException("some error happened");
        }
    }

}
