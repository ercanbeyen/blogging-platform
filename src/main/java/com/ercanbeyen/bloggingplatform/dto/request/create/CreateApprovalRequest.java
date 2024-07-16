package com.ercanbeyen.bloggingplatform.dto.request.create;

import jakarta.validation.constraints.NotNull;

public record CreateApprovalRequest(@NotNull(message = "Ticket id should not be null") Integer ticketId) {

}
