package com.ercanbeyen.bloggingplatform.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document
public class Post {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @DocumentReference
    private Author author;
    private String title;
    private String text;
    private String category;
    private List<String> tags;
    @DocumentReference
    private List<Comment> comments;
    private LocalDateTime latestChangeAt;
    @DocumentReference
    private List<Author> authorsLiked;
    @DocumentReference
    private List<Author> authorsDisliked;
}
