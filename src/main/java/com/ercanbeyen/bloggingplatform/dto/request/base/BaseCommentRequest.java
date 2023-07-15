package com.ercanbeyen.bloggingplatform.dto.request.base;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseCommentRequest {
    @NotBlank(message = "Text" + ResponseMessage.SHOULD_NOT_BLANK)
    private String text;
}
