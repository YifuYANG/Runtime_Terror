package app.bean;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenPool {

    //pool object maps <Long userId> to <String token>
    private Map<Long, String> pool = new HashMap<>();

    public void login(Long userId, String token) {
        pool.put(userId, token);
    }

    public void logout(String token) {
        pool.remove(token);
    }

    public boolean containsUserId(Long userId) {
        return pool.containsKey(userId);
    }

    public boolean containsToken(String token) {
        return pool.containsValue(token);
    }

    public String toString() {
        String str = "";
        int count = 1;
        for(Long userId: pool.keySet()) {
            str += count + ": " + userId + " -> " + pool.get(userId).toString() + "\n";
            count++;
        }
        return str;
    }

}
