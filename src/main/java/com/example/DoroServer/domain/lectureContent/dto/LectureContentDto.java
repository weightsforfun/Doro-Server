package com.example.DoroServer.domain.lectureContent.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureContentDto {

    @NotBlank
    private String kit; // 강의 사용 키트

    @NotBlank
    private String detail; // 강의 세부 구성

    @NotNull
    private String remark; // 강의 기타 사항

    @NotNull
    private String requirement; // 강의 자격 요건

    private Long id;




}
