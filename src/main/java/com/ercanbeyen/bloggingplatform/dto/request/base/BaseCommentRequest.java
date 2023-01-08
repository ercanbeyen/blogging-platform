package com.ercanbeyen.bloggingplatform.dto.request.base;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseCommentRequest {
    @NotBlank(message = "Text should not be blank")
    private String text;
}
