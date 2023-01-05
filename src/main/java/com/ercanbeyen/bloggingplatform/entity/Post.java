package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Post {
    @Id
    private String id;
    String author;
    String title;
    String text;
    String category;
    int numberOfLikes;
    List<String> tags;
    List<Comment> comments;
    private LocalDateTime latestChangeAt;

    public Post(String author, String title, String text, String category, int numberOfLikes, List<String> tags, List<Comment> comments, LocalDateTime latestChangeAt) {
        this.author = author;
        this.title = title;
        this.text = text;
        this.category = category;
        this.numberOfLikes = numberOfLikes;
        this.tags = tags;
        this.comments = comments;
        this.latestChangeAt = latestChangeAt;
    }
}
