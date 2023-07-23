package com.ercanbeyen.bloggingplatform.dto.request.update;

import com.ercanbeyen.bloggingplatform.annotation.PasswordRequest;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @PasswordRequest
    private String password;
    private String validationPassword;
}
