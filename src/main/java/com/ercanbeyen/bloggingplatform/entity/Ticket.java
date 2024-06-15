package com.ercanbeyen.bloggingplatform.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ticket {
    private Integer id;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
