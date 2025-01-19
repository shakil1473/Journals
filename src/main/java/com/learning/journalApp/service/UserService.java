package com.learning.journalApp.service;

import com.learning.journalApp.entity.User;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Helpers helpers;

    public ResponseEntity<User> saveUser(User User) {
        return new ResponseEntity<>(userRepository.save(User), HttpStatus.CREATED);
    }

    public User findUserByUsername(String username) {
        List<User> users = userRepository.findAll();
        User user = userRepository.findByUsername(username);
        return user;
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
}
