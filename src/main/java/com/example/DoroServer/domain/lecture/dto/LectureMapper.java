package com.example.DoroServer.domain.lecture.dto;


import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LectureMapper {


    //Lecture -> FindAllLectureRes
    FindAllLecturesRes toFindAllLecturesRes(Lecture lecture, LectureDate lectureDate);


}
