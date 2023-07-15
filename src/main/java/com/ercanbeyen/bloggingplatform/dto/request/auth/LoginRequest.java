package com.ercanbeyen.bloggingplatform.dto.request.auth;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username" + ResponseMessage.SHOULD_NOT_BLANK)
    private String username;
    @NotBlank(message = "Password" + ResponseMessage.SHOULD_NOT_BLANK)
    private String password;
}
