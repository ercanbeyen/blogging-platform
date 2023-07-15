package com.ercanbeyen.bloggingplatform.dto.request.auth;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    @NotBlank(message = "Username" + ResponseMessage.SHOULD_NOT_BLANK)
    private String username;
    @Email(
            regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = ResponseMessage.INVALID_EMAIL_FORMAT)
    private String email;
    @NotBlank(message = "Password" + ResponseMessage.SHOULD_NOT_BLANK)
    private String password;
}
