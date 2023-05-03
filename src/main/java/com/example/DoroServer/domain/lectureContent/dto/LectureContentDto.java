package com.example.DoroServer.domain.lectureContent.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import javax.validation.constraints.NotEmpty;
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


    private String kit; // 강의 사용 키트

    private String detail; // 강의 세부 구성

    private String remark; // 강의 기타 사항

    private String requirement; // 강의 자격 요건

    private Long id;




}
