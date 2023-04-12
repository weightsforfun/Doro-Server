package com.example.DoroServer.domain.user.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
@Builder
public class Degree {

    private String school; // 학교 정보
    private String studentId; // 학번
    private String major; // 전공
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus; // 학생 재적 상태 [ATTENDING, ABSENCE, GRADUATION]
}
