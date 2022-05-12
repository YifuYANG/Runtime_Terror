package app.dao;

import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AttemptLimitationDao {
    private static final long LOCK_DURATION = 20 * 60 * 1000; // 1 hours
    @Autowired
    private UserRepository userRepository;

    public void increaseFailedAttempts(User user) {
        int failAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(failAttempts);
        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    public void lock(User user) {
        user.setAccountLocked(true);
        user.setLock_time(new Date());
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        if (user.getLock_time() == null) {
            return true;
        } else {
            long lockTimeInMillis = user.getLock_time().getTime();
            long currentTimeInMillis = System.currentTimeMillis();
            if (lockTimeInMillis + LOCK_DURATION < currentTimeInMillis) {
                user.setAccountLocked(false);
                user.setLock_time(null);
                user.setFailedAttempts(0);
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        }
    }
}
