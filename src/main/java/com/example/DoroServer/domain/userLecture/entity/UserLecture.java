package com.example.DoroServer.domain.userLecture.entity;

import com.example.DoroServer.domain.lecture.entity.Lecture;
import com.example.DoroServer.domain.lecture.entity.LectureStatus;
import com.example.DoroServer.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserLecture {

    @Id
    @GeneratedValue
    @Column(name = "user_lecture_id")
    private Long id; // PK

    @Enumerated(EnumType.STRING)
    private TutorRole tutorRole; // 튜터 역할 [MAIN_TUTOR,SUB_TUTOR,STAFF]

    @Enumerated(EnumType.STRING)
    private TutorStatus tutorStatus;
    //== 연관관계 매핑 ==//

    // UserLecture와 User는 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // UserLecture와 Lecture는 다대일(Many-to-One) 관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public void changeTutorStatus(){
        if(this.tutorStatus == TutorStatus.ASSIGNED){
            this.tutorStatus=TutorStatus.WAITING;
        }
        else{
            this.tutorStatus=TutorStatus.ASSIGNED;
        }
    }
}
