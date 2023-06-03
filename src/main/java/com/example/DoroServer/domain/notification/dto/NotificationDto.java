package com.example.DoroServer.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@AllArgsConstructor
@Getter
public class NotificationDto {  // Fcm 서버에 보낼 객체

    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Apns {

        private Payload payload;

    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Payload {

        private Aps aps;

    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Aps {

        private String sound;   // 알림 울리는 사운드
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {

        private Notification notification;
        private String token;   // 전송할 대상 토큰
        private Data data;
        private Apns apns;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {

        private String title;   // 알림 제목
        private String body;    // 알림 내용
        private String image; // 알림 표기 사진
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private Long id;
        private String notiType;
    }
}

