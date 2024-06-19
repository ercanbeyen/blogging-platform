package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Approval {
    private Integer id; // Convert to uuid later
    private String authorId;
    private Ticket ticket;

    public Approval(String authorId, Ticket ticket) {
        this.authorId = authorId;
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        Integer ticketId = ticket.getId();

        return "Approval{" +
                "id=" + id +
                ", authorId='" + authorId + '\'' +
                ", ticketId=" + ticketId +
                '}';
    }
}
