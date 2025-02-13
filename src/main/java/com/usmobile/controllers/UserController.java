package com.usmobile.controllers;

import com.usmobile.models.User;
import com.usmobile.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.usmobile.repositories.UserCriteria.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-email")
    public List<User> getUserByEmail(@RequestParam String email) {
        return userService.findUsers(
            c -> filterByEmail(email)
        );
    }

    @GetMapping("/by-name")
    public List<User> getUsersByName(
        @RequestParam String firstName,
        @RequestParam String lastName) {
        return userService.findUsers(
            c -> filterByFirstName(firstName),
            c -> filterByLastName(lastName)
        );
    }

    @GetMapping("/{userId}")
    public Optional<User> getUserById(@PathVariable String userId) {
        return userService.findUserById(userId);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }
}
