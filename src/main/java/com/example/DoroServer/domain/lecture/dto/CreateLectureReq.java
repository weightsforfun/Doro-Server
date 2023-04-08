package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class CreateLectureReq {

    @NotEmpty
    private String title; // 강의 제목
    @NotEmpty
    private String institution; // 강의 기관

    @NotEmpty
    private String city; // 강의 도시

    @NotEmpty
    private String studentGrade; // 강의 대상

    @NotEmpty
    private String studentNumber; // 인원수

    @NotEmpty
    private String mainTutor; // 강의 메인 강사 수

    @NotEmpty
    private String subTutor; // 강의 서브 강사 수
    @NotEmpty
    private String staff; // 강의 스태프 수
    @NotEmpty
    private String payment; //강사 급여
    @NotEmpty
    private String time; // 시간
    @NotEmpty
    private List<LocalDate> lectureDates = new ArrayList<>(); // 강의 날짜
    @NotEmpty
    private LocalDateTime enrollStartDate; // 강의 등록 시작 날짜
    @NotEmpty
    private LocalDateTime enrollEndDate; // 강의 등록 종료 날짜

    private LectureContentDto lectureContentDto;
    public Lecture toEntity(){
        return Lecture.builder()
                .institution("institution")
                .city("city")
                .studentGrade("studentGrade")
                .studentNumber("studentNumber")
                .mainTutor("mainTutor")
                .subTutor("subTutor")
                .staff("staff")
                .payment("payment")
                .time("time")
                .lectureDate(
                        LectureDate.builder()
                                .lectureDates(lectureDates)
                                .enrollStartDate(enrollStartDate)
                                .enrollEndDate(enrollEndDate)
                                .build()
                )
                .build();
    }
}
