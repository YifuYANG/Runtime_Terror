package app.model;

import app.constant.UserLevel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue
    private Long userId;

    @NotBlank
    private String first_name, last_name, password, email, nationality, pps_number;
    private String date_of_birth;
    private long phone_number;
    private UserLevel userLevel;
    private boolean accountLocked = false;
    private int failedAttempts =0;
    private Date lock_time;
    private Date last_attempt;

    public User() {super();}
    public User(long userId, String first_name, String last_name, String password,
                String email_address, String nationality, String date_of_birth,
                String PPS_number, long phone_number, UserLevel userLevel, boolean accountNonLocked,
                int failedAttempts, Date lock_time, Date last_attempt) throws ParseException {
        super();
        this.userId = userId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email_address;
        this.nationality = nationality;
        setDate_of_birth(date_of_birth);
        this.pps_number = pps_number;
        this.phone_number = phone_number;
        this.userLevel = userLevel;
        this.accountLocked = accountNonLocked;
        this.failedAttempts = failedAttempts;
        this.lock_time = lock_time;
        this.last_attempt = last_attempt;
    }

    public Date getLast_attempt() {
        return last_attempt;
    }

    public void setLast_attempt(Date last_attempt) {
        this.last_attempt = last_attempt;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountNonLocked) {
        this.accountLocked = accountNonLocked;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth){
        this.date_of_birth = date_of_birth;
    }

    public String getPPS_number() {
        return pps_number;
    }

    public void setPPS_number(String pps_number) {
        this.pps_number = pps_number;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }
}
