package com.example.DoroServer.domain.announcement.dto;

import com.example.DoroServer.domain.announcement.entity.Announcement;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class AnnouncementReq {  // 공지 생성 시 전달 받는 객체

    @NotBlank(message = "제목이 비어있습니다.")
    private String title; // 공지 제목

    @NotBlank(message = "공지 내용이 비어있습니다.")
    private String body; // 공지 내용

    @NotBlank(message = "작성자 이름이 필요합니다.")
    private String writer; // 공지 작성자 이름
}
