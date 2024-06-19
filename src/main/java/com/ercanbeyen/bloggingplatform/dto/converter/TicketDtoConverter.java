package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.dto.TicketDto;
import com.ercanbeyen.bloggingplatform.entity.Approval;
import com.ercanbeyen.bloggingplatform.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public record TicketDtoConverter() {
    public TicketDto convert(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getDescription(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getApprovals()
                        .stream()
                        .map(Approval::getId)
                        .toList()
                );
    }
}
