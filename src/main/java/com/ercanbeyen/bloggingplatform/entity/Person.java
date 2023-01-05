package com.ercanbeyen.bloggingplatform.entity;

import com.ercanbeyen.bloggingplatform.constants.Location;
import com.ercanbeyen.bloggingplatform.constants.Gender;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class Person {
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

    public Person(String firstName, String lastName, String username, String email, String about, Gender gender, Location location, List<String> favoriteTopics, LocalDateTime createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.about = about;
        this.gender = gender;
        this.location = location;
        this.favoriteTopics = favoriteTopics;
        this.createdAt = createdAt;
    }
}
