package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Ticket {
    private Integer id;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Approval> approvals;
}
