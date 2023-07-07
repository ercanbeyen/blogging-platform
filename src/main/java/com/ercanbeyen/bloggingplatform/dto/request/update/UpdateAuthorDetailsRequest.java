package com.ercanbeyen.bloggingplatform.dto.request.update;

import com.ercanbeyen.bloggingplatform.constant.enums.Gender;
import com.ercanbeyen.bloggingplatform.constant.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UpdateAuthorDetailsRequest {
    @NotBlank(message = "First name should not be empty")
    private String firstName;
    @NotBlank(message = "Last name should not be empty")
    private String lastName;
    private String about;
    private Gender gender;
    private Location location;
    private List<String> favoriteTopics;

}