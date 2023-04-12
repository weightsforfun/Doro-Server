package com.example.DoroServer.domain.lecture.dto;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {

    private String title; // 강의 제목
    private String institution; // 강의 기관
    private String city; // 강의 도시
    private String studentGrade; // 강의 대상
    private String studentNumber; // 인원수
    private String  mainTutor; // 강의 메인 강사 수
    private String  subTutor; // 강의 서브 강사 수
    private String  staff; // 강의 스태프 수
    private String payment; //강사 급여
    private String time; // 시간
    private List<LocalDate> lectureDates = new ArrayList<>(); // 강의 날짜
    private LocalDateTime enrollStartDate; // 강의 등록 시작 날짜
    private LocalDateTime enrollEndDate; // 강의 등록 종료 날짜

    public static LectureDto fromEntity(Lecture lecture) {
        return LectureDto.builder()
                .title(lecture.getTitle())
                .institution(lecture.getInstitution())
                .city(lecture.getCity())
                .studentGrade(lecture.getStudentGrade())
                .studentNumber(lecture.getStudentNumber())
                .mainTutor(lecture.getMainTutor())
                .subTutor(lecture.getSubTutor())
                .staff(lecture.getStaff())
                .payment(lecture.getPayment())
                .time(lecture.getTime())
                .lectureDates(lecture.getLectureDate().getLectureDates())
                .enrollStartDate(lecture.getLectureDate().getEnrollStartDate())
                .enrollEndDate(lecture.getLectureDate().getEnrollEndDate())
                .build();
    }
}
