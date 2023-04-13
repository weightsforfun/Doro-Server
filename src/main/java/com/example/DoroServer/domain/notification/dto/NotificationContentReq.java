package com.example.DoroServer.domain.notification.dto;

import com.example.DoroServer.domain.notification.entity.Notification;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationContentReq {

    @NotBlank(message = "알림 제목을 입력하세요.")
    private String title;

    @NotBlank(message = "알림 내용을 입력하세요.")
    private String body;
    public Notification toEntity() {
        return Notification.builder()
                .title(title)
                .body(body)
                .isRead(false)
                .build();
    }
}
