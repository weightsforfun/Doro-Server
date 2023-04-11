package com.example.DoroServer.domain.userLecture.dto;


import com.example.DoroServer.domain.lecture.entity.Lecture;
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
    private String title;
    private LectureStatus lectureStatus;
    private LocalDateTime enrollEndDate;
    private String city;
    private String time;
    private TutorRole tutorRole;

    public static FindMyLecturesRes fromEntity(UserLecture userLecture){
        Lecture lecture=userLecture.getLecture();
        return FindMyLecturesRes.builder()
                .title(lecture.getTitle())
                .lectureStatus(lecture.getStatus())
                .enrollEndDate(lecture.getLectureDate().getEnrollEndDate())
                .city(lecture.getCity())
                .time(lecture.getTime())
                .tutorRole(userLecture.getTutorRole())
                .build();

    }

}
