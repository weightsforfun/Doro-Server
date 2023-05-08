package com.example.DoroServer.domain.notification.dto;

import com.example.DoroServer.domain.notification.entity.Notification;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationContentReq {   // 알림이 생성될 때, 토큰 없이 title과 body만 전달받는 객체

    @NotBlank(message = "알림 제목을 입력하세요.")
    private String title;

    @NotBlank(message = "알림 내용을 입력하세요.")
    private String body;

    private List<Long> userIds = new ArrayList<>();
    public Notification toEntity(Boolean isPublic) {
        return Notification.builder()
                .title(title)
                .body(body)
                .isPublic(isPublic)
                .isRead(false)
                .build();
    }
}
