package com.example.DoroServer.domain.lecture.dto;


import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.userLecture.dto.FindAllAssignedTutorsRes;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface LectureMapper {


    //Lecture -> FindAllLectureRes
    FindAllLecturesRes toFindAllLecturesRes(Lecture lecture, LectureDate lectureDate);

    //CreateLectureReq -> Lecture
    @Mapping(target = "status", constant = "RECRUITING")
    Lecture toLecture(CreateLectureReq createLectureReq);

    //Lecture -> LectureDto
    LectureDto toLectureDto(Lecture lecture);

    //LectureDto,LectureContentDto -> FindLectureRes
    FindLectureRes toFindLectureRes(LectureDto lectureDto, LectureContentDto lectureContentDto, List<FindAllAssignedTutorsRes> assignedTutors);

    void updateLecture(UpdateLectureReq updateLectureReq, @MappingTarget Lecture lecture);

}
