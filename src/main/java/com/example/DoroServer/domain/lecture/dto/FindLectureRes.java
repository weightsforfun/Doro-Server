package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class FindLectureRes {
    private LectureDto lectureDto;
    private LectureContentDto lectureContentDto;
}
