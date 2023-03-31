package com.example.DoroServer.domain.lectureContent.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LectureContent {

    @Id
    @GeneratedValue
    @Column(name = "lecture_content_id")
    private Long id; // PK

    private String title; // 강의 제목

    private String kit; // 강의 사용 키트

    private String detail; // 강의 세부 구성

    private String remark; // 강의 기타 사항

    private String requirement; // 강의 자격 요건
}
