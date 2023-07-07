package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.document.Notification;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.converter.NotificationDtoConverter;
import com.ercanbeyen.bloggingplatform.repository.NotificationRepository;
import com.ercanbeyen.bloggingplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationDtoConverter notificationDtoConverter;

    @KafkaListener(topics = {NotificationMessage.POST_NOTIFICATION, NotificationMessage.COMMENT_NOTIFICATION}, groupId = "group-id")
    public void listen(NotificationDto notificationDto) {
        Notification newNotification = Notification.builder()
                .fromAuthorId(notificationDto.getFromAuthorId())
                .toAuthorId(notificationDto.getToAuthorId())
                .description(notificationDto.getDescription())
                .topic(notificationDto.getTopic())
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(newNotification);
        log.info(String.format("Message receiver\n %s", newNotification.getDescription()));
    }

    @Override
    public List<NotificationDto> getNotifications(String authorId) {
        List<Notification> notifications = notificationRepository.findAll()
                .stream()
                .filter(notification -> notification.getToAuthorId().equals(authorId))
                .toList();

        return notifications.stream()
                .map(notificationDtoConverter::convert)
                .toList();
    }
}
