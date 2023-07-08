package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(String fromAuthorId, String toAuthorId);

    NotificationDto getNotification(String id);

    String deleteNotification(String id);
}
