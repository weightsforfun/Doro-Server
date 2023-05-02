package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
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
public class FindAllLecturesRes {
    private Long id;
    private String mainTitle;
    private String subTitle;
    private LectureStatus lectureStatus;
    private LocalDateTime enrollEndDate;
    private String city;
    private String place;
    private String mainTutor;
    private String subTutor;
    private String time;
    private List<LocalDateTime> lectureDates;







}
