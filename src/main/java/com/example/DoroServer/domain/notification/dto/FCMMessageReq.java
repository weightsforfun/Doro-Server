package com.example.DoroServer.domain.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FCMMessageReq {
    private String targetToken;
    private String title;
    private String body;
}
