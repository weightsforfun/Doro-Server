package com.example.DoroServer.domain.lecture.entity;

import com.example.DoroServer.domain.chat.entity.Chat;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.userLecture.entity.UserLecture;
import lombok.*;

import javax.persistence.*;
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

    @NotNull
    private String institution; // 강의 기관

    @NotNull
    private String city; // 강의 도시

    private String studentGrade; // 청강 학생 학년

    private String studentNumber; // 청강 학생 수

    @NotNull
    private Long mainTutor; // 강의 메인 강사

    @NotNull
    private Long subTutor; // 강의 서브 강사

    private Long staff; // 강의 스태프

    @NotNull
    @Enumerated(EnumType.STRING)
    private LectureStatus status; // 강의 상태 [RECRUITING, ALLOCATION_COMP,FINISH]

    private LocalDateTime lectureDate; // 강의 날짜

    private LocalDateTime enrollStateDate; // 강의 등록 시작 날짜

    private LocalDateTime enrollEndDate; // 강의 등록 종료 날짜

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
