package com.ercanbeyen.bloggingplatform.dto.request.create;

import com.ercanbeyen.bloggingplatform.dto.request.base.BaseAuthorRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateAuthorRequest extends BaseAuthorRequest {
    private String firstName;
    private String lastName;
    @NotBlank(message = "Username should not be null")
    private String username;
    @Email(message = "Email should be unique")
    private String email;
}
