package app.bean;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenPool {

    private Map<String, Object> pool;

    public void init() {
        pool = new HashMap<>();
    }

    public void login() {
        //UUID
        //pool.put(token, user)
    }

    public void logout(String token) {
        pool.remove(token);
    }

    public boolean contains(String token) {
        return pool.containsKey(token);
    }

}
