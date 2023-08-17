package com.example.DoroServer.domain.notification.dto;

import com.google.firebase.messaging.*;

public class NotificationConfig {
    public static AndroidConfig androidConfig(NotificationContentReq notificationContentReq){
        return AndroidConfig.builder()
                .setNotification(
                        AndroidNotification.builder()
                                .setTitle(notificationContentReq.getTitle())
                                .setBody(notificationContentReq.getBody())
                                .build()
                )
                .build();
    }
    public static ApnsConfig apnsConfig(NotificationContentReq notificationContentReq){
        return ApnsConfig.builder()
                .setAps(
                        Aps.builder()
                                .setAlert(
                                        ApsAlert.builder()
                                                .setTitle(notificationContentReq.getTitle())
                                                .setBody(notificationContentReq.getBody())
                                                .build()
                                )
                                .setSound("default")
                                .build()
                )
                .build();
    }
}
