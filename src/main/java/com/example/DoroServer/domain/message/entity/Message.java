package com.example.DoroServer.domain.message.entity;

import com.example.DoroServer.domain.base.BaseEntity;
import com.example.DoroServer.domain.chat.entity.Chat;
import com.example.DoroServer.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id; //PK

    @NotNull
    private String content; // 메시지 내용

    //== 연관관계 매핑 ==//

    // Message와 User는 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    // Message와 User는 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
