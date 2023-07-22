package com.ercanbeyen.bloggingplatform.dto.request.update;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String password;
    private String validationPassword;
}
