package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lecture.entity.LectureStatus;

import java.time.LocalDateTime;

public class ShowAllLectureDto {

    private String title;
    private com.example.DoroServer.domain.lecture.entity.LectureStatus LectureStatus;
    private LocalDateTime enrollEndDate;
    private String city;

    //강의 날짜 리스트

    private String mainTutor;
    private String subTutor;


}
