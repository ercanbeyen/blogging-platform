package com.ercanbeyen.bloggingplatform.document;

import com.ercanbeyen.bloggingplatform.constant.Location;
import com.ercanbeyen.bloggingplatform.constant.Gender;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document
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
    private LocalDateTime createdAt;
}
