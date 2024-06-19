package com.ercanbeyen.bloggingplatform.dto.request.base;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseTicketRequest {
    @NotBlank(message = "Description" + ResponseMessage.SHOULD_NOT_BLANK)
    String description;
}
