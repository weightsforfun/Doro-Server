package com.example.DoroServer.domain.announcement.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Builder
@AllArgsConstructor
public class AnnouncementRes {

    private Long id; // 공지 id

    private String title; // 공지 제목

    private String body; // 공지 내용

    private String  picture; // 공지 첨부 사진 - 업로드 사이즈 제한 추가 필요

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    public AnnouncementRes(Long id, String title, String body, String picture) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.picture = picture;
    }
}
