package com.example.DoroServer.domain.userLecture.dto;


import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLectureMapper {
    @Mapping(source = "userLecture.id", target = "id")
    @Mapping(source="lecture.id",target="lectureId")
    FindMyLecturesRes toFindMyLecturesRes(Lecture lecture, UserLecture userLecture);

    @Mapping(source = "userLecture.id", target = "id")
    @Mapping(source = "user.id",target = "userId")
    FindAllTutorsRes toFindAllTutorsRes(UserLecture userLecture, User user);

    @Mapping(source = "userLecture.id", target = "id")
    @Mapping(source = "user.id",target = "userId")
    FindAllAssignedTutorsRes toFindAllAssignedTutorsRes(UserLecture userLecture,User user);
}
