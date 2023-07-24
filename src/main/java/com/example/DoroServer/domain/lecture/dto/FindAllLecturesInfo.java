package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import java.time.LocalDate;
import java.util.List;

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
public class FindAllLecturesInfo {
    private Long id;
    private String mainTitle;
    private String subTitle;
    private LectureStatus status;
    private LocalDate enrollEndDate;
    private String city;
    private String place;
    private String mainTutor;
    private String subTutor;
    private String staff;
    private String time;
    private List<LocalDate> lectureDates;







}
