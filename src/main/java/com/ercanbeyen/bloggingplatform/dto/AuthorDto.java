package com.ercanbeyen.bloggingplatform.dto;

import com.ercanbeyen.bloggingplatform.constant.enums.Gender;
import com.ercanbeyen.bloggingplatform.util.Location;
import com.ercanbeyen.bloggingplatform.entity.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AuthorDto extends RepresentationModel<EntityModel<AuthorDto>> {
    private String id;
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
