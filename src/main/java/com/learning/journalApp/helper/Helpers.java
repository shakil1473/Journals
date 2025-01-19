package com.learning.journalApp.helper;

import com.learning.journalApp.entity.Journal;
import com.learning.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Helpers {
    @Autowired
    public final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   public void addDates(Journal journal) {
        journal.setCreateDate(LocalDate.now());
        updateModifiedDate(journal);
    }

    public void updateModifiedDate(Journal journal) {
        journal.setModifiedDate(LocalDate.now());
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getPasswordEncoder(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

}
