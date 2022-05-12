package app.controller;

import app.annotation.access.RestrictUserAccess;
import app.bean.TokenPool;
import app.constant.UserLevel;
import app.exception.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.jasypt.util.text.StrongTextEncryptor;

import javax.validation.Valid;
import java.text.ParseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "register";
    }
    // Create a new User
    @PostMapping
    public String RegisterUser(@ModelAttribute("newUser") User newUser, BindingResult result) {
        newUser.setUserLevel(UserLevel.CLIENT);

        if(checkForEmpty(newUser)){
            return "redirect:/register?empty";
        }

        User existing = userRepository.findByUserEmail(newUser.getEmail());
        if(existing != null){
            result.rejectValue("Email", null, "An account already exists for this email");
        }
        if(!ppsValidator(newUser.getPPS_number())){
            return "redirect:/register?ppsnError";
        }
        if(!emailValidator(newUser.getEmail())){
            return "redirect:/register?invalidEmail";
        }
        if (result.hasErrors() || !emailValidator(newUser.getEmail())){
            return "redirect:/register?tryAgain";
        }
        if(!passwordValidator(newUser.getPassword())){
            return "redirect:/register?passwordError";
        }
        if(userRoleValidator(newUser.getUserLevel().toString())){
            /**
            I am not sure if I did correctly, I am validating data at back side so attacker wouldn't register admin account using burp
             */
            return "redirect:/register?registrationError";
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        /**
         PPS, data of birth, and phone number should be encoded before storing in to DB
         */
        newUser.setPPS_number(ppsnEncoder.encrypt((newUser.getPPS_number())));
        System.out.println(ppsnEncoder.decrypt(newUser.getPPS_number()));
        userRepository.save(newUser);
        return "redirect:/register?success";
    }

    private boolean checkForEmpty(User newUser) {
        if( newUser.getFirst_name() == null || newUser.getLast_name()==null||
            newUser.getNationality() == null || newUser.getDate_of_birth() ==null||
            newUser.getPPS_number() == null || newUser.getPhone_number() == 0||
            newUser.getEmail()==null || newUser.getUserLevel()==null||
            newUser.getPassword() == null)
        {
            return true;
        }else {
            return false;
        }
    }

    //validate email type at back end in case of attack may bypass front side
    //copy of Yifu's code
    private Boolean emailValidator(String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Boolean userRoleValidator(String userLevel){
        return userLevel.equals("ADMIN");
    }

    private Boolean passwordValidator(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()?])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private Boolean ppsValidator(String ppsNumber){
        /** Six Digits then two letters for PPSN*/
        String regex = "\\d{6}[a-zA-Z]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ppsNumber);
        return matcher.matches();
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
