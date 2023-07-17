package com.example.DoroServer.domain.userLecture.dto;


import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.TutorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindMyLecturesRes {
    private Long id;
    private String mainTitle;
    private String subTitle;
    private LectureStatus status;
    private LectureDate lectureDate;
    private List<LocalDate> lectureDates;
    private String city;
    private String place;
    private String time;
    private TutorRole tutorRole;
    private TutorStatus tutorStatus;
    private Long lectureId;



}
