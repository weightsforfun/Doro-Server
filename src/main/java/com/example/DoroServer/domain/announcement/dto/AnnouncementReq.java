package com.example.DoroServer.domain.announcement.dto;

import com.example.DoroServer.domain.announcement.entity.Announcement;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AnnouncementReq {  // 공지 생성 시 전달 받는 객체

    @NotBlank(message = "제목이 비어있습니다.")
    private String title; // 공지 제목

    @NotBlank(message = "공지 내용이 비어있습니다.")
    private String body; // 공지 내용

    private String  picture; // 공지 첨부 사진 - 업로드 사이즈 제한 추가 필요

    public Announcement toEntity() {
        return Announcement.builder()
                .title(title)
                .body(body)
                .picture(picture)
                .build();
    }
}
