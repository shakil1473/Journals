package com.learning.journalApp.controller;

import com.learning.journalApp.entity.User;
import com.learning.journalApp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://web.postman.co");
    }
    
    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @GetMapping
    public List<User> getAllUsres() {
        return userService.findAll();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserById(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        return userService.updateUserPassword(user);
    }

    @DeleteMapping("/delete/{username}")
    public void deleteUser(@PathVariable String username) {
    }

    @DeleteMapping("/delete")
    public void deleteAllUsers() {
    }
}
