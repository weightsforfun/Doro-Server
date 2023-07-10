package com.example.DoroServer.domain.lectureContent.entity;

import javax.persistence.GenerationType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LectureContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_content_id")
    private Long id; // PK

    private String kit; // 강의 사용 키트

    private String detail; // 강의 세부 구성

    private String remark; // 강의 기타 사항

    private String requirement; // 강의 자격 요건

    private String content;
}
