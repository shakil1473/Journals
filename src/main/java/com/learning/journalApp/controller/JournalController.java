package com.learning.journalApp.controller;

import com.learning.journalApp.entity.Journal;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.service.JournalService;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;

    @Autowired
    private Helpers helpers;

    @GetMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://web.postman.co");
    }

    @PostMapping
    public ResponseEntity<?> createJournal(@RequestBody Journal journal) {
        return journalService.saveJournal(journal);
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalsOfUser() {
        return journalService.findAllUserJournals();
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<Journal> getJournalById(@PathVariable ObjectId journalId) {
        return journalService.findJournalById(journalId);
    }

    @PutMapping("/update/{journalId}")
    public ResponseEntity<Journal> updateJournal(@RequestBody Journal journal, @PathVariable ObjectId journalId) {
        return journalService.updateJournal(journalId, journal);
    }

    @DeleteMapping("/delete/{journalId}")
    public ResponseEntity<?> deleteJournal(@PathVariable ObjectId journalId) {
        return journalService.delete(journalId);
    }

    @DeleteMapping("/delete")
    public void deleteAllJournals() {
        journalService.deleteAll();
    }
}
