package com.ercanbeyen.bloggingplatform.dto.request.create;

import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import jakarta.validation.constraints.NotBlank;

public record CreateApprovalRequest(
        @NotBlank(message = "Author id" + ResponseMessage.SHOULD_NOT_BLANK)
        String authorId,
        Integer ticketId) {
}
