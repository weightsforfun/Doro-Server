package com.example.DoroServer.domain.user.dto;

import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import com.example.DoroServer.domain.user.entity.User;
import java.time.LocalDate;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FindUserRes {

    private String name; // 사용자 이름
    private LocalDate birth; // 생년월일
    private Gender gender; // 사용자 성별
    private String phone; // 사용자 전화번호
    private int generation; // 사용자 기수
    private String role; // 사용자 직책
    private String profileImg; // 사용자 이미
    private String school; // 학교 정보
    private String studentId; // 학번
    private String major; // 전공
    private StudentStatus studentStatus; // 학생 재적 상태 [ATTENDING, ABSENCE, GRADUATION]
    private static FindUserRes fromEntity(User user) {
        return FindUserRes.builder()
                .name(user.getName())
                .birth(user.getBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .generation(user.getGeneration())
                .role(user.getRole())
                .profileImg(user.getProfileImg())
                .school(user.getDegree().getSchool())
                .studentId(user.getDegree().getStudentId())
                .major(user.getDegree().getMajor())
                .studentStatus(user.getDegree().getStudentStatus())
                .build();
    }
}
