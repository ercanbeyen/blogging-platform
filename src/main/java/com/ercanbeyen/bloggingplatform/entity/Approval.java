package com.ercanbeyen.bloggingplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Approval {
    private String id;
    private String authorId;
    private Ticket ticket;

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
