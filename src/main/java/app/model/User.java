package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    private Long userId;
    @NotBlank
    private String first_name, last_name, password, email, nationality, date_of_birth;
    private long PPS_number, phone_number;

    public User() {super();}
    public User(long userId, String first_name, String last_name, String password,
                String email_address, String nationality, String date_of_birth,
                long PPS_number, long phone_number) {
        super();
        this.userId = userId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email_address;
        this.nationality = nationality;
        this.date_of_birth = date_of_birth;
        this.PPS_number = PPS_number;
        this.phone_number = phone_number;
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

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public long getPPS_number() {
        return PPS_number;
    }

    public void setPPS_number(long PPS_number) {
        this.PPS_number = PPS_number;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }
}
