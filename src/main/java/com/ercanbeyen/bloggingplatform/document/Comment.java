package com.ercanbeyen.bloggingplatform.document;

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
    private String postId;
    private String author;
    private String text;
    private LocalDateTime latestChangeAt;
}
