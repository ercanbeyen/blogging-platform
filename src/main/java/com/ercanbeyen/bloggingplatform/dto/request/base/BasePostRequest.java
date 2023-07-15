package com.ercanbeyen.bloggingplatform.dto.request.base;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BasePostRequest {
    @NotBlank(message = "Title" + ResponseMessage.SHOULD_NOT_BLANK)
    private String title;
    @NotBlank(message = "Text" + ResponseMessage.SHOULD_NOT_BLANK)
    private String text;
    @NotBlank(message = "Category" + ResponseMessage.SHOULD_NOT_BLANK)
    private String category;
    private List<String> tags;
}
