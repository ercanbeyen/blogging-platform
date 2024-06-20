package com.ercanbeyen.bloggingplatform.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TicketDto(
        Integer id,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> approvalIds) {

}
