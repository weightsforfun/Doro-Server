package com.example.DoroServer.domain.lecture.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureDate {

    @ElementCollection
    @CollectionTable(name = "lecture_date", joinColumns =
    @JoinColumn(name = "lecture_id"))
    private List<LocalDateTime> lectureDates = new ArrayList<>(); // 강의 날짜
//
    private LocalDateTime enrollStateDate; // 강의 등록 시작 날짜
    //
    private LocalDateTime enrollEndDate; // 강의 등록 종료 날짜
}
