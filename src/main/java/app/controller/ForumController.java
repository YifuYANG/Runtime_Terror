package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.dao.ForumPostDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/forum")
@Slf4j
public class ForumController {

    @Autowired
    private TokenPool tokenPool;

    @Autowired
    private ForumPostDao forumPostDao;

    @GetMapping
    public String forumPage(Model model) {
        model.addAttribute("posts", forumPostDao.findAllToVO());
        return "forum";
    }

    @PostMapping("/post")
    @RestrictUserAccess(requiredLevel = UserLevel.ANY)
    @ResponseBody
    public String createPost(@RequestHeader("token") String token,
                             @RequestBody Map<String, String> data) {
        Long userId = tokenPool.getUserIdByToken(token);
        if(!forumPostDao.checkCoolDown(userId)) return "You just posted a question less than 1 minute ago.";
        String content = data.get("content");
        forumPostDao.save(userId, content);
        log.info("User ID = " + userId + " created new post: " + content);
        return "Your question has been submitted successfully!";
    }

}
