package app.controller.dto;

import app.constant.UserLevel;
import org.hibernate.validator.constraints.Email;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.util.Date;
/** Not used at the moment could be used in
further development for registering users*/
public class UserRegistrationDto {

    @NotBlank
    private String first_name;

    @NotBlank
    private String lastName;

    @NotBlank
    private String nationality;

    @NotBlank
    private Date date_of_birth;

    @NotBlank
    private long PPS_number;

    @NotBlank
    private long phone_number;

    @NotBlank
    private UserLevel userLevel;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @Email
    @NotBlank
    private String email;

    @Email
    @NotBlank
    private String confirmEmail;

    @AssertTrue
    private Boolean terms;

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
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

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }

    public Boolean getTerms() {
        return terms;
    }

    public void setTerms(Boolean terms) {
        this.terms = terms;
    }
}
