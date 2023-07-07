package com.ercanbeyen.bloggingplatform.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto implements Serializable {
    private String fromAuthorId;
    private String toAuthorId;
    private String description;
    private String topic;
    private LocalDateTime createdAt;
}
