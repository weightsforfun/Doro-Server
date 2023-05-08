package com.example.DoroServer.domain.userLecture.dto;


import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureDate;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.userLecture.entity.TutorRole;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String city;
    private String place;
    private String time;
    private TutorRole tutorRole;



}
