package com.ercanbeyen.bloggingplatform.dto.request.base;

import com.ercanbeyen.bloggingplatform.constant.Gender;
import com.ercanbeyen.bloggingplatform.constant.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BaseAuthorRequest {
    @NotBlank(message = "First name should not be empty")
    private String firstName;
    @NotBlank(message = "Last name should not be empty")
    private String lastName;
    private String about;
    private Gender gender;
    private Location location;
    private List<String> favoriteTopics;
}
