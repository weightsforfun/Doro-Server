package com.example.DoroServer.domain.announcement.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnnouncementRes {  // 공지 조회 후 전달하는 객체

    private Long id; // 공지 id

    private String title; // 공지 제목

    private String body; // 공지 내용

    private String  picture; // 공지 첨부 사진 - 업로드 사이즈 제한 추가 필요

    private LocalDateTime createdAt; // 공지 생성 시간

    private LocalDateTime lastModifiedAt; // 공지 수정 시간
}
