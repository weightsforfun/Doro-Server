package com.example.DoroServer.domain.announcement.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementMultipartReq {

    @NotBlank(message = "제목이 비어있습니다.")
    private String title; // 공지 제목

    @NotBlank(message = "공지 내용이 비어있습니다.")
    private String body; // 공지 내용

    @NotBlank(message = "작성자 이름이 필요합니다.")
    private String writer; // 공지 작성자 이름

    private MultipartFile picture;
}
