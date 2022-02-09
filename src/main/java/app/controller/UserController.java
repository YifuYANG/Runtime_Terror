package app.controller;

import app.controller.dto.UserRegistrationDto;
import app.exception.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.*;
import static java.util.Calendar.DATE;

@Controller
@RequestMapping("/registration")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "RegisterUser";
    }
    // Create a new User
    @PostMapping("/register")
    public String RegisterUser(@ModelAttribute("newUser") User newUser) {
        userRepository.save(newUser);
        return "redirect:/registration?success";
    }
    // Get All Users
    @GetMapping("/Users")
    public List<User> getAllUsers() throws ParseException {
//        userRepository.save(new User(1, "Tom", "Furlong", "1234", "t@emal",
//               "irish", "07-APR-1999", 909, 065));
        return userRepository.findAll();
    }

    @GetMapping("/addnew")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newuser";
    }

    // Get a Single User by ID
    @GetMapping("/users/{id}")
    public User getBookById(@PathVariable(value = "id") Long bookId) throws UserNotFoundException {
        return userRepository.findById(bookId)
                .orElseThrow(() -> new UserNotFoundException(bookId));
    }

    // Update an Existing User
    @PutMapping("/users/{id}")
    public User updateBook(@PathVariable(value="id") Long userId, @Valid @RequestBody User userDetails)
            throws UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.setEmail(userDetails.getEmail());
        user.setPhone_number(userDetails.getPhone_number());
        User updatedBook = userRepository.save(user);

        return updatedBook;
    }

    // Delete a Book
    //only admins should be able to do this action
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable(value="id") Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
