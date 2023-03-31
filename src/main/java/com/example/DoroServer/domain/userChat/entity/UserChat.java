package com.example.DoroServer.domain.userChat.entity;

import com.example.DoroServer.domain.chat.entity.Chat;
import com.example.DoroServer.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChat {

    @Id
    @GeneratedValue
    @Column(name = "user_chat_id")
    private Long id; // PK

    //== 연관관계 매핑 ==//

    //UserChat 과 User는 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //UserChat 과 Chat은 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
