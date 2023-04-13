package com.example.DoroServer.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class NotificationReq {
    private String targetToken;
    private String title;
    private String body;
}
