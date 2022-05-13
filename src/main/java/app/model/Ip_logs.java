package app.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ip_logs")
public class Ip_logs {
    @Id
    @GeneratedValue
    private Long ipId;
    private String method;
    private String url;
    private String ip;
    private boolean accountLocked = false;
    private int failedAttempts =1;
    private Date lock_time;
    private Date last_attempt;

    public Ip_logs(String method, String url, String ip) {
        this.method = method;
        this.url = url;
        this.ip = ip;
    }

    public Ip_logs(String method, String url, String ip, boolean accountLocked,int failedAttempts, Date lock_time, Date last_attempt) {
        this.method = method;
        this.url = url;
        this.ip = ip;
        this.accountLocked =accountLocked;
        this.failedAttempts = failedAttempts;
        this.lock_time = lock_time;
        this.last_attempt = last_attempt;
    }

    public Long getIpId() {
        return ipId;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Date getLock_time() {
        return lock_time;
    }

    public void setLock_time(Date lock_time) {
        this.lock_time = lock_time;
    }

    public Date getLast_attempt() {
        return last_attempt;
    }

    public void setLast_attempt(Date last_attempt) {
        this.last_attempt = last_attempt;
    }

    public void setIpId(Long ipId) {
        this.ipId = ipId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
