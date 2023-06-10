package com.example.DoroServer.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationReq {  // FCM Message 생성 위해 전달할 객체

    private String targetToken;
    private String title;
    private String body;
    private Long id; // 알림에 해당하는 공지글 or 알림의 id 값
}
