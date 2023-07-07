package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.document.Notification;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = {NotificationMessage.POST_NOTIFICATION, NotificationMessage.COMMENT_NOTIFICATION}, groupId = "group-id")
    public void listen(NotificationDto notificationDto) {
        Notification newNotification = Notification.builder()
                .authorId(notificationDto.getAuthorId())
                .description(notificationDto.getDescription())
                .topic(notificationDto.getTopic())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(newNotification);
        log.info(String.format("Message receiver\n %s", newNotification.getDescription()));
    }
}
