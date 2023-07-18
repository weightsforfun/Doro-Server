package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.userLecture.dto.FindAllAssignedTutorsRes;
import com.example.DoroServer.domain.userLecture.dto.FindAllTutorsRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class FindLectureRes {
    private LectureDto lectureDto;
    private LectureContentDto lectureContentDto;
    private List<FindAllAssignedTutorsRes> assignedTutors;
}
