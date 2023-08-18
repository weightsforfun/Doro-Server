package com.example.DoroServer.domain.notification.entity;

import com.example.DoroServer.domain.base.BaseEntity;
import com.example.DoroServer.domain.notification.dto.NotificationRes;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id; // PK

    @NotBlank(message = "알림 제목을 입력하세요.")
    private String title; // 알림 제목

    @NotBlank(message = "알림 내용을 입력하세요")
    private String body; // 알림 내용

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

//    private Boolean isRead; // 알림 읽음 유무

    private Long targetId;

    // Notification을 NotificationRes객체로 변환해주는 메소드
    public NotificationRes toRes() {
        return NotificationRes.builder()
                .id(id)
                .title(title)
                .body(body)
                .subscriptionType(subscriptionType)
//                .isRead(isRead)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .targetId(targetId)
                .build();
    }

    // 알림 확인 후 읽음 처리하는 메소드
//    public void isReadTrue() {
//        this.isRead = true;
//    }
}
