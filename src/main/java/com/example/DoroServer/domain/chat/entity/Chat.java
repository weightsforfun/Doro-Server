package com.example.DoroServer.domain.chat.entity;

import com.example.DoroServer.domain.userChat.entity.UserChat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private Long id; // PK

    private int totalCount; // 채팅방 정원


}
