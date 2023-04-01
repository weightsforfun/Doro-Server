package com.example.DoroServer.domain.userNotification.entity;

import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.user.entity.User;
import lombok.*;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue
    @Column(name = "user_notification_id")
    private Long id; // PK

    private LocalDateTime expirationPeriod; // 만료 기간

    //== 연관관계 매핑 ==//

    // UserNotification과 User는 다대일(Many-to-One) 관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // UserNotification과 Notification은 다대일(Many-to-One) 관계
    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;


}
