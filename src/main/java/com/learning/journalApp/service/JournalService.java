package com.learning.journalApp.service;

import com.learning.journalApp.entity.Journal;
import com.learning.journalApp.entity.User;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class JournalService {
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public ResponseEntity<?> saveJournal(Journal journal, String username) {
        try {
            User user = userService.findUserByUsername(username);
            if(user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // first we need to save journal. Otherwise, journal won't have id, and it will give null pointer error while saving
            // in the user
            Journal newJournal = journalRepository.save(journal);
            userService.addJournals(user, newJournal);
            return new ResponseEntity<>(newJournal, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> findAllUserJournals(String username) {
        User user = userService.findUserByUsername(username);
        if(user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NO_CONTENT);
        }
        List<Journal> journals = user.getJournalEntries();
        if(journals.isEmpty())
            return new ResponseEntity<>("Sorry! You don't have any Journals", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(journals, HttpStatus.OK);
    }

    public Journal findJournalById(String id) {
        return journalRepository.findById(id).orElse(null);
    }
    public ResponseEntity<Journal> findById(String id) {
        ObjectId objectId = new ObjectId(id);
        Journal journal = findJournalById(id);
        if(journal == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(journal, HttpStatus.OK);
    }

    public ResponseEntity<Journal> update(String id, Journal journal) {
        Journal oldJournal = findJournalById(id);
        if (oldJournal != null) {
            oldJournal.setTitle(journal.getTitle());
            oldJournal.setContent(journal.getContent());
            Helpers.updateModifiedDate(journal);

            return new ResponseEntity<>(journalRepository.save(oldJournal), HttpStatus.OK);
        }

        Helpers.addDates(journal);
        return new ResponseEntity<>(journalRepository.save(journal), HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> delete(String id, String username) {
        Journal journal = findJournalById(id);
        if(journal != null) {
            userService.removeJournals(username, journal);
            journalRepository.delete(journal);
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    public void deleteAll() {
        journalRepository.deleteAll();
    }
}
