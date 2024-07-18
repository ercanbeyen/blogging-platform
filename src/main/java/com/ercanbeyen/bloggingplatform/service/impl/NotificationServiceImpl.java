package com.ercanbeyen.bloggingplatform.service.impl;

import com.ercanbeyen.bloggingplatform.constant.values.EntityName;
import com.ercanbeyen.bloggingplatform.constant.enums.RoleName;
import com.ercanbeyen.bloggingplatform.constant.messages.NotificationMessage;
import com.ercanbeyen.bloggingplatform.constant.messages.ResponseMessage;
import com.ercanbeyen.bloggingplatform.entity.Author;
import com.ercanbeyen.bloggingplatform.entity.Notification;
import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.dto.converter.NotificationDtoConverter;
import com.ercanbeyen.bloggingplatform.exception.data.DataForbidden;
import com.ercanbeyen.bloggingplatform.exception.data.DataNotFound;
import com.ercanbeyen.bloggingplatform.repository.NotificationRepository;
import com.ercanbeyen.bloggingplatform.service.NotificationService;
import com.ercanbeyen.bloggingplatform.util.SecurityUtil;
import com.ercanbeyen.bloggingplatform.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationDtoConverter notificationDtoConverter;

    @KafkaListener(topics = {NotificationMessage.POST_NOTIFICATION, NotificationMessage.COMMENT_NOTIFICATION}, groupId = "group-id")
    public void listen(NotificationDto notificationDto) {
        Notification newNotification = createNotification(notificationDto);
        log.info("Message receiver\n {}", newNotification.getDescription());
    }

    @Override
    public List<NotificationDto> getNotifications(String fromAuthorId, String toAuthorId) {
        Predicate<Notification> filteringAuthors;

        if (fromAuthorId == null && toAuthorId != null)  { // User Condition
            filteringAuthors = notification -> notification.getToAuthorId().equals(toAuthorId);
        } else { // Admin Condition
            Author loggedInAuthor = SecurityUtil.getLoggedInAuthor();

            boolean isAdmin = loggedInAuthor.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(roleName -> roleName.equals(RoleName.ADMIN.name()));

            if (!isAdmin) {
                throw new DataForbidden(ResponseMessage.NOT_AUTHORIZED);
            }

            filteringAuthors = notification ->
                    (StringUtils.isBlank(fromAuthorId) || notification.getFromAuthorId().equals(fromAuthorId)) &&
                            (StringUtils.isBlank(toAuthorId) || notification.getToAuthorId().equals(toAuthorId));
        }


        return notificationRepository.findAll()
                .stream()
                .filter(filteringAuthors)
                .map(notificationDtoConverter::convert)
                .toList();
    }

    @Override
    public NotificationDto getNotification(String id) {
        Notification notificationInDb = notificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.NOTIFICATION, id)));

        return notificationDtoConverter.convert(notificationInDb);
    }

    @Override
    public String deleteNotification(String id) {
        boolean doesExist = notificationRepository.findAll()
                .stream()
                .anyMatch(notification -> notification.getId().equals(id));

        if (!doesExist) {
            throw new DataNotFound(String.format(ResponseMessage.NOT_FOUND, EntityName.NOTIFICATION, id));
        }

        notificationRepository.deleteById(id);

        return String.format(ResponseMessage.SUCCESS, EntityName.NOTIFICATION, id, ResponseMessage.Operation.DELETED);
    }

    private Notification createNotification(NotificationDto notificationDto) {
        Notification newNotification = Notification.builder()
                .fromAuthorId(notificationDto.getFromAuthorId())
                .toAuthorId(notificationDto.getToAuthorId())
                .description(notificationDto.getDescription())
                .topic(notificationDto.getTopic())
                .createdAt(TimeUtil.calculateNow())
                .build();

        return notificationRepository.save(newNotification);
    }
}
