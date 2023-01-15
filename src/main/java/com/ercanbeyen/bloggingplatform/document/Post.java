package com.ercanbeyen.bloggingplatform.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
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
    private int numberOfLikes;
    private List<String> tags;
    @DocumentReference
    private List<Comment> comments;
    private LocalDateTime latestChangeAt;
}
