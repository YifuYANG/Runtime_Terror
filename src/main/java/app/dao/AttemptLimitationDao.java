package app.dao;

import app.model.Ip_logs;
import app.repository.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AttemptLimitationDao {
    private static final long LOCK_DURATION = 20 * 60 * 1000; // 20 mines

    @Autowired
    private IpAddressRepository ipAddressRepository;


    public void increaseFailedAttempts(Ip_logs ip_logs){
        if(ipAddressRepository.findByIpAddress(ip_logs.getIp())!=null){
            int failAttempts = ip_logs.getFailedAttempts() + 1;
            ip_logs.setFailedAttempts(failAttempts);
        }
    }

    public void resetFailedAttempts(Ip_logs ip_logs) {
        ip_logs.setFailedAttempts(0);
        ipAddressRepository.save(ip_logs);
    }

    public void setLastAttempt(Ip_logs ip_logs){
        ip_logs.setLast_attempt(new Date());
        ipAddressRepository.save(ip_logs);
    }
    public void lock(Ip_logs ip_logs) {
        ip_logs.setAccountLocked(true);
        ip_logs.setLock_time(new Date());
        ipAddressRepository.save(ip_logs);
    }

    public void resetAttemptsAfterPeriodOfTime(Ip_logs ip_logs){
        if (ip_logs.getLast_attempt() != null) {
            long lastAttemptTimeInMillis = ip_logs.getLast_attempt().getTime();
            long currentTimeInMillis = System.currentTimeMillis();
            if (lastAttemptTimeInMillis + LOCK_DURATION < currentTimeInMillis) {
                ip_logs.setFailedAttempts(0);
                ipAddressRepository.save(ip_logs);
            }
        }
    }
    public boolean unlockWhenTimeExpired(Ip_logs ip_logs) {
        if (ip_logs.getLock_time() == null) {
            return true;
        } else {
            long lockTimeInMillis = ip_logs.getLock_time().getTime();
            long currentTimeInMillis = System.currentTimeMillis();
            if (lockTimeInMillis + LOCK_DURATION < currentTimeInMillis) {
                ip_logs.setAccountLocked(false);
                ip_logs.setLock_time(null);
                ip_logs.setFailedAttempts(0);
                ipAddressRepository.save(ip_logs);
                return true;
            } else {
                return false;
            }
        }
    }
}
