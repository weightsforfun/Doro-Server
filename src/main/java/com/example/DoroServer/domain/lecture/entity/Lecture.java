package com.example.DoroServer.domain.lecture.entity;

import com.example.DoroServer.domain.base.BaseEntity;
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
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "lecture_id")
    private Long id; // PK

    private String title; // 강의 제목

    @NotBlank
    private String institution; // 강의 기관

    @NotBlank
    private String city; // 강의 도시

    @NotBlank
    private String studentGrade; // 강의 대상

    @NotBlank
    private String studentNumber; // 인원수

    @NotBlank
    private String  mainTutor; // 강의 메인 강사 수

    @NotBlank
    private String  subTutor; // 강의 서브 강사 수

    private String  staff; // 강의 스태프 수

    private String payment; //강사 급여

    private String time; // 시간

    @Enumerated(EnumType.STRING)
    @NotNull
    private LectureStatus status; // 강의 상태 [RECRUITING, ALLOCATION_COMP,FINISH]

    @Embedded
    private LectureDate lectureDate; // 강의 날짜 관련 [lectureStartDates, enrollStateDate, enrollEndDate]

    //== 연관관계 매핑 ==//

    // Lecture와 LectureContent는 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_content_id")
    private LectureContent lectureContent; // 강의 내용

    // Lecture와 Chat은 일대일(One-to-One) 관계
    @OneToOne(fetch = LAZY)
    @JoinColumn(name ="chat_id")
    private Chat chat; // 강의 채팅


}
