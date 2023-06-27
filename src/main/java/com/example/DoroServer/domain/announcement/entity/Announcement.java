package com.example.DoroServer.domain.announcement.entity;

import com.example.DoroServer.domain.announcement.dto.AnnouncementReq;
import com.example.DoroServer.domain.announcement.dto.AnnouncementRes;
import com.example.DoroServer.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Announcement extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "announcement_id")
    private Long id; //PK

    @NotBlank
    private String title; // 공지 제목

    @NotBlank
    private String body; // 공지 내용

    @NotBlank
    private String writer; // 공지 작성자 이름

    private String picture; // 공지 첨부 사진 - 업로드 사이즈 제한 추가 필요

    // Announcement를 AnnouncementRes 객체로 변환하는 메소드
    public AnnouncementRes toRes() {
        return AnnouncementRes.builder()
                .id(id)
                .title(title)
                .body(body)
                .writer(writer)
                .picture(picture)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }

    // Announcement 수정 메소드
    // Picture는 따로 Update
    public void update(AnnouncementReq announcementReq) {
        this.title = announcementReq.getTitle();
        this.body = announcementReq.getBody();
    }
}
