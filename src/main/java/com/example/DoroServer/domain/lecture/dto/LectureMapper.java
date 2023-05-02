package com.example.DoroServer.domain.lecture.dto;


import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import org.mapstruct.Mapper;

import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface LectureMapper {


    //Lecture -> FindAllLectureRes
    FindAllLecturesRes toFindAllLecturesRes(Lecture lecture, LectureDate lectureDate);

    //CreateLectureReq -> Lecture
    Lecture toLecture(CreateLectureReq createLectureReq);

    //Lecture -> LectureDto
    LectureDto toLectureDto(Lecture lecture);

    //LectureDto,LectureContentDto -> FindLectureRes
    FindLectureRes toFindLectureRes(LectureDto lectureDto, LectureContentDto lectureContentDto);

    void updateLecture(UpdateLectureReq updateLectureReq, @MappingTarget Lecture lecture);

}
