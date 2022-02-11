package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.constant.UserLevel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class IndexController {

    @GetMapping(value = {"/index", "/"})
    public String index() {
        return "index";
    }

    @RestrictUserAccess(requiredLevel = UserLevel.CLIENT)
    @PostMapping("/create-appointment")
    @ResponseBody
    public String book(@RequestHeader("token") String token, @RequestBody Map<String, Object> form) {
        System.out.println(form.get("brand"));
        System.out.println(form.get("date"));
        System.out.println(form.get("center"));
        return "OK";
    }


}
