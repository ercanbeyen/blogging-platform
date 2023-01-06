package com.ercanbeyen.bloggingplatform.dto;

import com.ercanbeyen.bloggingplatform.constants.Gender;
import com.ercanbeyen.bloggingplatform.constants.Location;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@Builder
public class AuthorDto {
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
}
