package com.example.DoroServer.domain.userNotification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue
    @Column(name = "user_notification_id")
    private Long id; // PK

//    private Long userId;

//    private Long notificationId;

    private LocalDateTime expirationPeriod; // 만료 기간
}
