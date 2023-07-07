package com.ercanbeyen.bloggingplatform.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto implements Serializable {
    private String authorId;
    private String description;
    private String topic;
}
