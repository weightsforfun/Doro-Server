package com.example.DoroServer.domain.lectureContent.dto;

import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LectureContentMapper {
    //LectureContent -> LectureContentDto
    LectureContentDto toLectureContentDto(LectureContent lectureContent);
    LectureContent toLectureContent(LectureContentDto lectureContentDto);
}
