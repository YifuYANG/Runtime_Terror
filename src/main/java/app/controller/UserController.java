package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.CommonStringPool;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.exception.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.numeric.StrongIntegerNumberEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.jasypt.util.text.StrongTextEncryptor;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.text.ParseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/register")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenPool tokenPool;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private StrongTextEncryptor ppsnEncoder;

    @Autowired
    private StrongTextEncryptor dobEncoder;

    @Autowired
    private StrongIntegerNumberEncryptor phoneNumberEncoder;

    @Autowired
    private CommonStringPool commonStringPool;

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @GetMapping
    public String showRegistrationForm(Model model) throws FileNotFoundException {
        return "register";
    }
    // Create a new User
    @PostMapping
    public String RegisterUser(@ModelAttribute("newUser") User newUser, BindingResult result) throws ParseException {
        newUser.setUserLevel(UserLevel.CLIENT);

        if(checkForEmpty(newUser)){
            return "redirect:/register?empty";
        }
        User existing = userRepository.findByUserEmail(newUser.getEmail());
        if(existing != null){
            result.rejectValue("Email", null, "An account already exists for this email");
        }
        if(!specialCharacterFilter(newUser.getFirst_name()) || !specialCharacterFilter(newUser.getLast_name())){
            return "redirect:/register?usernameError";
        }

        if(!specialCharacterFilter(newUser.getNationality())){
            return "redirect:/register?nationalityError";
        }

        if(!ppsValidator(newUser.getPPS_number())){
            return "redirect:/register?ppsnError";
        }
        if(findByPPSN(newUser.getPPS_number())) {
            return "redirect:/register?ppsnExistError";
        }
        if(!emailValidator(newUser.getEmail())){
            return "redirect:/register?invalidEmail";
        }
        if(result.hasErrors() || !emailValidator(newUser.getEmail())){
            return "redirect:/register?tryAgain";
        }
        if(!passwordValidator(newUser.getPassword(),newUser.getLast_name(),newUser.getFirst_name())){
            return "redirect:/register?passwordError";
        }
        if(!phoneValidator(newUser.getPhone_number())) {
            return "redirect:/register?phoneNumberError";
        }
        if(!dobValidator(newUser.getDate_of_birth())){
            return "redirect:/register?dobError";
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        /**
         PPS, data of birth, and phone number should be encoded before storing in to DB
         */
        String dob = String.valueOf(newUser.getDate_of_birth());
        newUser.setDate_of_birth(dobEncoder.encrypt(dob));
        newUser.setPPS_number(ppsnEncoder.encrypt((newUser.getPPS_number())));
        BigInteger bigPhoneNumber = BigInteger.valueOf(newUser.getPhone_number());
        long phoneNum = phoneNumberEncoder.encrypt(bigPhoneNumber).longValue();
        newUser.setPhone_number(phoneNum);
        /** To decrypt use the decrypt() method that jasypt provides */

        userRepository.save(newUser);
        return "redirect:/register?success";
    }

    private boolean checkForEmpty(User newUser) {
        return newUser.getFirst_name() == null || newUser.getLast_name() == null ||
                newUser.getNationality() == null || newUser.getDate_of_birth() == null ||
                newUser.getPPS_number() == null || newUser.getPhone_number() == 0 ||
                newUser.getEmail() == null || newUser.getUserLevel() == null ||
                newUser.getPassword() == null;
    }

    //validate email type at back end in case of attack may bypass front side
    //copy of Yifu's code
    private Boolean emailValidator(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Boolean passwordValidator(String password, String lastname, String firstname){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()?])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches() && !password.contains(lastname) && !password.contains(firstname) && password.length()>8
                && !commonStringPool.ifContainCommonString(password);
    }

    private Boolean ppsValidator(String ppsNumber){
        /** Seven Digits then one letters for PPSN*/
        String regex = "\\d{7}[a-zA-Z]{1}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ppsNumber);
        return matcher.matches();
    }

    private Boolean phoneValidator(long phoneNumber){
        String regex = "^(8)[0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(phoneNumber));
        return matcher.matches();
    }

    private Boolean dobValidator(String dob){
        String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(dob));
        return matcher.matches();
    }

    private Boolean specialCharacterFilter(String username){
        String regex = "^[^\\\\<>]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    private List<String> findAllPPSN(){
        return userRepository.findAllPPS();
    }

    private Boolean findByPPSN(String ppsNumber) {
        List<String> ppsn_list = findAllPPSN();
        Collections.reverse(ppsn_list);
        for (String ppsn:ppsn_list){
            if(!ppsValidator(ppsn) || !(ppsn.getClass().getName().equals("in"))) continue;
            String check = ppsnEncoder.decrypt(ppsn);
            if (check.equals(ppsNumber)){
                return true;
            }
        }
        return false;
    }
    // Get All Users
    @RestrictUserAccess(requiredLevel = UserLevel.ADMIN)
    @GetMapping("/Users")
    public List<User> getAllUsers( @RequestHeader("token") String token) throws ParseException {
//        userRepository.save(new User(1, "Tom", "Furlong", "1234", "t@emal",
//               "irish", "07-APR-1999", 909, 065));
        log.info("Admin ID = " + tokenPool.getUserIdByToken(token) + " queried all users information.");
        return userRepository.findAll();
    }

    // Get a Single User by ID
    @RestrictUserAccess(requiredLevel = UserLevel.ADMIN)
    @GetMapping("/users/{id}")
    public User getBookById(
            @RequestHeader("token") String token,
            @PathVariable(value = "id") Long bookId
    ) throws UserNotFoundException {
        log.info("Admin ID = " + tokenPool.getUserIdByToken(token) + " queried a book with ID = " + bookId);
        return userRepository.findById(bookId)
                .orElseThrow(() -> new UserNotFoundException(bookId));
    }

    // Update an Existing User
    @PutMapping("/users/{id}")
    @RestrictUserAccess(requiredLevel = UserLevel.ADMIN)
    public User updateBook(
            @RequestHeader("token") String token,
            @PathVariable(value="id") Long userId,
            @Valid @RequestBody User userDetails
    ) throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setEmail(userDetails.getEmail());
        user.setPhone_number(userDetails.getPhone_number());
        User updatedBook = userRepository.save(user);
        log.info("Admin ID = " + tokenPool.getUserIdByToken(token) + " updated an user account with ID = " + userId);
        return updatedBook;
    }

    // Delete a Book
    //only admins should be able to do this action
    @RestrictUserAccess(requiredLevel = UserLevel.ADMIN)
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteBook(
            @RequestHeader("token") String token,
            @PathVariable(value="id") Long userId) throws UserNotFoundException {
        log.info("Admin ID = " + tokenPool.getUserIdByToken(token)
                + " deleted an user with ID = " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

}
