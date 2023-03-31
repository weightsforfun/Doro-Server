package com.example.DoroServer.domain.chat.entity;

import com.example.DoroServer.domain.userChat.entity.UserChat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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


    //== 연관관계 매핑 ==//

    //Chat과 UserChat은 일대다(One-to-Many) 관계
    @OneToMany(mappedBy = "chat")
    private List<UserChat> userChats = new ArrayList<>();

}
