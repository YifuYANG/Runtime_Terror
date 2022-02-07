package app.controller;

import app.exception.UserNotFoundException;
import app.model.User;
import app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.*;
import static java.util.Calendar.DATE;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    // Get All Users
    @GetMapping("/Users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // Create a new User
    @PostMapping("/register")
    public User RegisterUser(@Valid @RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    // Get a Single Book
    @GetMapping("/users/{id}")
    public User getBookById(@PathVariable(value = "id") Long bookId) throws UserNotFoundException {
        return userRepository.findById(bookId)
                .orElseThrow(() -> new UserNotFoundException(bookId));
    }

    // Update an Existing Book
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
