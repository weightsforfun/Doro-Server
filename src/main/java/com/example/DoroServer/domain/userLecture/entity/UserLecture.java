package com.example.DoroServer.domain.userLecture.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLecture {

    @Id
    @GeneratedValue
    @Column(name = "user_lecture_id")
    private Long id; // PK

//    private Long userId; // User.id FK

//    private Long lectureId; // Lecture.id FK

    @Enumerated(EnumType.STRING)
    private TutorRole tutorRole; // 튜터 역할 [MAIN_TUTOR,SUB_TUTOR,STAFF]

}
