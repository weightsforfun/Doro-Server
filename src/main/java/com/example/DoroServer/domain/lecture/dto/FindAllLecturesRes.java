package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import java.time.LocalDateTime;
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

    private String title;
    private LectureStatus lectureStatus;
    private LocalDateTime enrollEndDate;
    private String city;
    private String mainTutor;
    private String subTutor;
    private String time;

    public static FindAllLecturesRes fromEntity(Lecture lecture) {
        return FindAllLecturesRes.builder()
                .title(lecture.getTitle())
                .lectureStatus(lecture.getStatus())
                .enrollEndDate(lecture.getLectureDate().getEnrollEndDate())
                .city(lecture.getCity())
                .mainTutor(lecture.getMainTutor())
                .subTutor(lecture.getSubTutor())
                .title(lecture.getTime())
                .build();
    }





}