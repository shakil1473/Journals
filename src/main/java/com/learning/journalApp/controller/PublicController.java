package com.learning.journalApp.controller;

import com.learning.journalApp.entity.User;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private Helpers helpers;;

    @PostMapping
    public void createUser(@RequestBody User user) {
        String encodedPassword = helpers.getPasswordEncoder(user);
        user.setPassword(encodedPassword);
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);
        userService.saveUser(user);
    }
}
