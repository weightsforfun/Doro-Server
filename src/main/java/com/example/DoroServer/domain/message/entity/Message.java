package com.example.DoroServer.domain.message.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

//    private Long chatId; // 채팅방 FK

//    private Long sender; // 작성한 사용자

    private String content; // 메시지 내용

}
