package com.example.DoroServer.domain.notification.entity;

import com.example.DoroServer.domain.base.BaseEntity;
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
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id; // PK

    @NotBlank
    private String title; // 알림 제목

    @NotBlank
    private String content; // 알림 내용

}
