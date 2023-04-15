package com.example.DoroServer.domain.lecture.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class FindAllLecturesCond {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;

}
