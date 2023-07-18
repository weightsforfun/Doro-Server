package com.example.DoroServer.domain.lectureContent.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLectureContentReq {
    private String kit; // 강의 사용 키트

    private String detail; // 강의 세부 구성

    private String remark; // 강의 기타 사항

    private String requirement; // 강의 자격 요건

    private String content;
}
