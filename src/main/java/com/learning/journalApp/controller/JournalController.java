package com.learning.journalApp.controller;

import com.learning.journalApp.entity.Journal;
import com.learning.journalApp.helper.Helpers;
import com.learning.journalApp.service.JournalService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;

    @GetMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://web.postman.co");
    }

    @PostMapping("{username}")
    public ResponseEntity<?> createJournal(@RequestBody Journal journal, @PathVariable String username) {
        Helpers.addDates(journal);
        return journalService.saveJournal(journal, username);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalsOfUser(@PathVariable String username) {
        return journalService.findAllUserJournals(username);
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<Journal> getJournalById(@PathVariable String journalId) {
        return journalService.findById(journalId);
    }

    @PutMapping("/update")
    public ResponseEntity<Journal> updateJournal(@RequestBody Journal journal, @PathVariable String journalId) {
        return journalService.update(journalId, journal);
    }

    @DeleteMapping("/delete/{journalId}")
    public ResponseEntity<?> deleteJournal(@PathVariable String journalId, @RequestParam String username) {
        return journalService.delete(journalId, username);
    }

    @DeleteMapping("/delete")
    public void deleteAllJournals() {
        journalService.deleteAll();
    }
}
