package com.example.DoroServer.domain.message.entity;

import com.example.DoroServer.domain.chat.entity.Chat;
import com.example.DoroServer.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id; //PK

    private String content; // 메시지 내용

    //== 연관관계 매핑 ==//

    // Message와 Chat은 다대일(Many-to-One) 관계
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    // Message와 User는 다대일(Many-to-One) 관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
