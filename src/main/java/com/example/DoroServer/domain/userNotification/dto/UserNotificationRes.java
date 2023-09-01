package com.example.DoroServer.domain.userNotification.dto;


import com.example.DoroServer.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserNotificationRes {

    private Long userNotificationId;

    private Notification notification;

    private Boolean isRead;

}
