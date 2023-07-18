package com.example.DoroServer.domain.chat.entity;

import com.example.DoroServer.domain.base.BaseEntity;
import com.example.DoroServer.domain.message.entity.Message;
import com.example.DoroServer.domain.userChat.entity.UserChat;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id; // PK

    private int totalCount; // 채팅방 정원

    //== 연관관계 매핑 ==//


}
