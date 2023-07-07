package com.ercanbeyen.bloggingplatform.service;

import com.ercanbeyen.bloggingplatform.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(String id);
}
