package com.example.DoroServer.domain.lecture.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Embeddable
public class LectureDate {

    private List<LocalDateTime> lectureDates = new ArrayList<>(); // 강의 날짜
//
    private LocalDateTime enrollStateDates; // 강의 등록 시작 날짜
    //
    private LocalDateTime enrollEndDates; // 강의 등록 종료 날짜
}
