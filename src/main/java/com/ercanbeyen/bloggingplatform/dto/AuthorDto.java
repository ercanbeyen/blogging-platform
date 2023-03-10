package com.ercanbeyen.bloggingplatform.dto;

import com.ercanbeyen.bloggingplatform.constant.Gender;
import com.ercanbeyen.bloggingplatform.constant.Location;
import com.ercanbeyen.bloggingplatform.document.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class AuthorDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String about;
    private Gender gender;
    private Location location;
    private List<String> favoriteTopics;
    private LocalDateTime createdAt;
    private Set<Role> roles;
}
