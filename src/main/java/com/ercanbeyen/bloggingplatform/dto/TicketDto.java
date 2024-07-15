package com.ercanbeyen.bloggingplatform.dto;

import com.ercanbeyen.bloggingplatform.constant.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TicketDto(
        Integer id,
        String description,
        TicketStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> approvalIds) {

}
