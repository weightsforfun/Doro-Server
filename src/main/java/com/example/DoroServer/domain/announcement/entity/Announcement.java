package com.example.DoroServer.domain.announcement.entity;

import com.example.DoroServer.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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

    private String title; // 공지 제목

    private String content; // 공지 내용

    private String  picture; // 공지 첨부 사진 - 업로드 사이즈 제한 추가 필요

}
