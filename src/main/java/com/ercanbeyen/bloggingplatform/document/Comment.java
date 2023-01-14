package com.ercanbeyen.bloggingplatform.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Data
@Builder
@Document
public class Comment {
    @Id
    private String id;
    @DocumentReference
    private Author author;
    private String text;
    private LocalDateTime latestChangeAt;
}
