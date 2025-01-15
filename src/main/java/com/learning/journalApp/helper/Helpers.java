package com.learning.journalApp.helper;

import com.learning.journalApp.entity.Journal;

import java.time.LocalDate;

public class Helpers {
    static public void addDates(Journal journal) {
        journal.setCreateDate(LocalDate.now());
        updateModifiedDate(journal);
    }

    static public void updateModifiedDate(Journal journal) {
        journal.setModifiedDate(LocalDate.now());
    }
}
