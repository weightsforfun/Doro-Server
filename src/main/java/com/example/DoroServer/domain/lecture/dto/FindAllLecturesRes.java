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

    @NotEmpty
    private String title;
    @NotEmpty
    private LectureStatus lectureStatus;
    @NotEmpty
    private LocalDateTime enrollEndDate;
    @NotEmpty
    private String city;
    @NotEmpty
    private String mainTutor;
    @NotEmpty
    private String subTutor;

    public static FindAllLecturesRes fromEntity(Lecture lecture) {
        return FindAllLecturesRes.builder()
                .title(lecture.getTitle())
                .lectureStatus(lecture.getStatus())
                .enrollEndDate(lecture.getLectureDate().getEnrollEndDate())
                .city(lecture.getCity())
                .mainTutor(lecture.getMainTutor())
                .subTutor(lecture.getSubTutor())
                .build();
    }





}
