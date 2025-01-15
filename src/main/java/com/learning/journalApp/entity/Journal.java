package com.learning.journalApp.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Journals")
@Data
public class Journal {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private LocalDate createDate;
    private LocalDate modifiedDate;
}