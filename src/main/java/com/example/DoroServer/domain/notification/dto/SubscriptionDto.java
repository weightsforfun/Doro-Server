package com.example.DoroServer.domain.notification.dto;


import com.example.DoroServer.domain.notification.entity.NotificationType;
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
public class SubscriptionDto {
    @NotBlank(message = "알림 유형을 선택해 주세요")
    private NotificationType notificationType;

    @NotBlank
    private String fcmToken;

}
