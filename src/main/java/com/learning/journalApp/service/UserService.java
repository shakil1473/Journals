package com.learning.journalApp.service;

import com.learning.journalApp.entity.User;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Helpers helpers;

    public ResponseEntity<?> createUser(User user) {
        String encodedPassword = helpers.getPasswordEncoder(user);
        user.setPassword(encodedPassword);
        ArrayList<String> roles = new ArrayList<>();
        roles.add("USER");
        user.setRoles(roles);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<?> updateUserLoginInfo(User user) {
        Authentication authentication = helpers.getAuthentication();
        User userInDb = findUserByUsername(authentication.getName());
        if(userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(helpers.getPasswordEncoder(user));
            userRepository.save(userInDb);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void addOrRemoveJournalToUser(User user) {
        userRepository.save(user);
    }

    public ResponseEntity<?> deleteUser() {
        Authentication authentication = helpers.getAuthentication();
        User userInDb = findUserByUsername(authentication.getName());
        userRepository.delete(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> createAdmin(User user) {
        if(helpers.isAdmin()) {
            String encodedPassword = helpers.getPasswordEncoder(user);
            user.setPassword(encodedPassword);
            ArrayList<String> roles = new ArrayList<>();
            roles.add("ADMIN");
            user.setRoles(roles);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
