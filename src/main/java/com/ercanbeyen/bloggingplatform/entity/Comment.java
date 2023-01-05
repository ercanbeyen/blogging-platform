package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Comment {
    @Id
    private String id;
    private String author;
    private String text;
    LocalDateTime latestChangeAt;

    public Comment(String author, String text, LocalDateTime latestChangeAt) {
        this.author = author;
        this.text = text;
        this.latestChangeAt = latestChangeAt;
    }
}
