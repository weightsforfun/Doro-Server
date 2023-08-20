package com.example.DoroServer.domain.userNotification.entity;

import com.example.DoroServer.domain.notification.entity.Notification;
import com.example.DoroServer.domain.user.entity.User;
import lombok.*;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_notification_id")
    private Long id; // PK

    // UserNotification과 User는 다대일(Many-to-One) 관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    // UserNotification과 Notification은 다대일(Many-to-One) 관계
    // UserNotification이 만료되어 삭제되면 Notification도 삭제됨
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @ColumnDefault("false")
    private Boolean isRead; // 알림 읽음 유무

    public void changeIsRead(){
        this.isRead=true;
    }
}
