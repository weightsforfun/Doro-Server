package com.example.DoroServer.domain.lecture.entity;

import com.example.DoroServer.domain.chat.entity.Chat;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Lecture {

    @Id
    @GeneratedValue
    @Column(name = "lecture_id")
    private Long id; // PK

    @NotBlank
    private String institution; // 강의 기관

    @NotBlank
    private String city; // 강의 도시

    @NotBlank
    private String studentGrade; // 청강 학생 학년

    @NotBlank
    private String studentNumber; // 청강 학생 수

    @NotBlank
    private String  mainTutor; // 강의 메인 강사 수

    @NotBlank
    private String  subTutor; // 강의 서브 강사 수

    private String  staff; // 강의 스태프 수

    @Enumerated(EnumType.STRING)
    @NotNull
    private LectureStatus status; // 강의 상태 [RECRUITING, ALLOCATION_COMP,FINISH]

    @Embedded
    private LectureDate lectureDate; // 강의 날짜 관련 [lectureStartDates, enrollStateDates, enrollEndDates]

    //== 연관관계 매핑 ==//

    // Lecture와 LectureContent는 일대일(One-to-One) 관계
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "lecture_content_id")
    private LectureContent lectureContent; // 강의 내용

    // Lecture와 Chat은 일대일(One-to-One) 관계
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name ="chat_id")
    private Chat chat; // 강의 채팅

    // Lecture와 UserLecture는 일대다(One-to-Many) 관계
    @OneToMany(mappedBy = "lecture")
    private List<UserLecture> userLectures = new ArrayList<>();

}
