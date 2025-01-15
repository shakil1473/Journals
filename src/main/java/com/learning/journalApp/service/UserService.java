package com.learning.journalApp.service;

import com.learning.journalApp.entity.Journal;
import com.learning.journalApp.entity.User;
import com.learning.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<User> saveUser(User User) {
        return new ResponseEntity<>(userRepository.save(User), HttpStatus.CREATED);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public ResponseEntity<User>findByUsername(String username) {
       return new ResponseEntity<>(findUserByUsername(username), HttpStatus.OK);
    }

    public ResponseEntity<?> updateUserPassword(User user) {
        User userInDb = findUserByUsername(user.getUsername());
        if(userInDb != null) {
            userInDb.setPassword(user.getPassword());
            userRepository.save(userInDb);

            return new ResponseEntity<>(userInDb, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> addJournals(User user, Journal journal) {
        user.getJournalEntries().add(journal);
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> removeJournals(String username, Journal journal) {
        User user = findUserByUsername(username);
        user.getJournalEntries().removeIf(x->x.getId().equals(journal.getId()));
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.NOT_FOUND);
    }
    public void delete(String username) {
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
