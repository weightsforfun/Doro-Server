package com.example.DoroServer.domain.user.entity;

import com.example.DoroServer.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue // strategy = GenerationType.IDENTITY ??
    @Column(name = "user_id")
    private Long id; // PK

    private String name; // 사용자 이름

    private int age; // 사용자 나이

    private String gender; // 사용자 성별

    @NotNull(message = "아이디는 필수입니다.")
    private String username; // 사용자 아이디

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password; // 사용자 비밀번호

    private String phone; // 사용자 전화번호

    @Embedded
    private Degree degree; // 사용자 학교 정보 [school, studentId, major, studentStatus]

    @NotNull(message = "사용자 기수가 필요합니다.")
    private int generation; // 사용자 기수

    @NotNull(message = "사용자 직책이 필요합니다.")
    private String role; // 사용자 직책

    @NotNull(message = "사용자 인증코드가 필요합니다.")
    private Long certificationCode; // 사용자 인증코드

    private String profileImg; // 이미지 파일은 바이너리로 변환?

}
