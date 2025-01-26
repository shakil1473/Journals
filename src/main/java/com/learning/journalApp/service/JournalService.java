package com.learning.journalApp.service;

import com.learning.journalApp.entity.Journal;
import com.learning.journalApp.entity.User;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.repository.JournalRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JournalService {
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private Helpers helpers;

    @Transactional
    public ResponseEntity<?> saveJournal(Journal journal) {
        try {
            Authentication authentication = helpers.getAuthentication();
            User user = userService.findUserByUsername(authentication.getName());
            if(user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // first we need to save journal. Otherwise, journal won't have id, and it will give null pointer error
            // while saving in the user
            helpers.addDates(journal);
            Journal newJournal = journalRepository.save(journal);

            user.getJournalEntries().add(newJournal);
            userService.addOrRemoveJournalToUser(user);
            return new ResponseEntity<>(newJournal, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Save Journal failed", e, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> findAllUserJournals() {
        Authentication authentication = helpers.getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        if(user == null) {
            log.error("User not found");
            return new ResponseEntity<>("User not found", HttpStatus.NO_CONTENT);
        }
        List<Journal> journals = user.getJournalEntries();
        if(journals.isEmpty()) {
            log.error("User doesn't have any journal entries");
            return new ResponseEntity<>("Sorry! You don't have any Journals", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(journals, HttpStatus.OK);
    }
    
    public ResponseEntity<Journal> findJournalById(ObjectId id) {
        Authentication authentication = helpers.getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        List<Journal> journals = getJournalsForUser(id, user);
        if(!journals.isEmpty()) {
            Optional<Journal> journal = journalRepository.findById(id);
            if(journal.isPresent()) {
                return new ResponseEntity<>(journal.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Journal> updateJournal(ObjectId id, Journal journal) {
        Authentication authentication = helpers.getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        List<Journal> journals = getJournalsForUser(id, user);
        if(!journals.isEmpty()) {
            Journal oldJournal = journalRepository.findById(id).orElse(null);
            if(oldJournal != null) {
                oldJournal.setTitle(journal.getTitle());
                oldJournal.setContent(journal.getContent());
                helpers.updateModifiedDate(journal);
                log.info("Journal updated");
                return new ResponseEntity<>(journalRepository.save(oldJournal), HttpStatus.OK);
            }
        }

        log.info("Journal not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Transactional
    public ResponseEntity<?> delete(ObjectId id) {
        Authentication authentication = helpers.getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        List<Journal> journals = getJournalsForUser(id, user);
        if(!journals.isEmpty()) {
            Journal journal = journalRepository.findById(id).orElse(null);
            if(journal != null) {
                user.getJournalEntries().remove(journal);
                userService.addOrRemoveJournalToUser(user);

                return new ResponseEntity<>(journalRepository.save(journal), HttpStatus.OK);
            }
        }

        log.info("Not Found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void deleteAll() {
        journalRepository.deleteAll();
    }

    private List<Journal> getJournalsForUser(ObjectId id, User user) {
        return user.getJournalEntries().stream().filter(journal -> journal.getId().equals(id)).collect(Collectors.toList());
    }
}
