package com.ercanbeyen.bloggingplatform.dto.converter;

import com.ercanbeyen.bloggingplatform.document.Notification;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoConverter {
    public NotificationDto convert(Notification notification) {
        return NotificationDto.builder()
                .fromAuthorId(notification.getFromAuthorId())
                .toAuthorId(notification.getToAuthorId())
                .description(notification.getDescription())
                .topic(notification.getTopic())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
