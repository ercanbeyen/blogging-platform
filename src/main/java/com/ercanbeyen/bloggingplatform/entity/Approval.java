package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;

@Data
public class Approval {
    private Integer id; // Convert to uuid later
    private String authorId;
    private Ticket ticket;
    private Integer ticketId;

}
