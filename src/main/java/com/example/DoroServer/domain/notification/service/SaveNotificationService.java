package com.example.DoroServer.domain.notification.service;

import com.example.DoroServer.domain.notification.dto.NotificationContentReq;
import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.notification.entity.NotificationType;
import com.example.DoroServer.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveNotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Long saveNotification(NotificationContentReq notificationContentReq,
                                 NotificationType notificationType, Long announcementId) {
        Notification notification = notificationContentReq.toEntity(notificationType, announcementId);
        notificationRepository.save(notification);

        return notification.getId();
    }
}
