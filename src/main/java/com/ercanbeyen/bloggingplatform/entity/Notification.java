package com.ercanbeyen.bloggingplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Notification {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String fromAuthorId;
    private String toAuthorId;
    private String description;
    private String topic;
    private LocalDateTime createdAt;
}
