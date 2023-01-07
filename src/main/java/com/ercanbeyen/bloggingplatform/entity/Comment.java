package com.ercanbeyen.bloggingplatform.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class Comment {
    @Id
    private String id;
    private String author;
    private String text;
    LocalDateTime latestChangeAt;
}
