package com.ercanbeyen.bloggingplatform.controller;

import com.ercanbeyen.bloggingplatform.dto.NotificationDto;
import com.ercanbeyen.bloggingplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Object> getNotifications(
            @RequestParam(required = false, value = "from") String fromAuthorId,
            @RequestParam(required = false, value = "to") String toAuthorId) {
        return ResponseEntity.ok(notificationService.getNotifications(fromAuthorId, toAuthorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNotification(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(notificationService.getNotification(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNotification(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }
}
