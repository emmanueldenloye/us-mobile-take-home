package com.usmobile.controllers;

import com.usmobile.models.User;
import com.usmobile.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User updatedUser) {
        return userService.updateUser(userId, updatedUser);
    }
}
