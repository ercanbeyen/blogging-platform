package com.ercanbeyen.bloggingplatform.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document
public class Post {
    @Id
    private String id;
    private String authorId;
    private String title;
    private String text;
    private String category;
    private int numberOfLikes;
    private List<String> tags;
    @DocumentReference
    private List<Comment> comments;
    private LocalDateTime latestChangeAt;
}
