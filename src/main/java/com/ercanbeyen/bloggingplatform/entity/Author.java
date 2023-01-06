package com.ercanbeyen.bloggingplatform.entity;

import com.ercanbeyen.bloggingplatform.constants.Location;
import com.ercanbeyen.bloggingplatform.constants.Gender;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
@Builder
public class Author {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String about;
    private Gender gender;
    private Location location;
    private List<String> favoriteTopics;
    LocalDateTime createdAt;
}
