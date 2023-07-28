package com.example.DoroServer.domain.user.dto;

import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import java.time.LocalDate;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@EqualsAndHashCode
public class UpdateUserReq {
    @NotBlank
    private String school;

    @NotBlank
    private String studentId;

    @NotBlank
    private String major;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

    @NotBlank
    private String phone; // 사용자 전화번호

    @NotNull
    private int generation; // 사용자 기수

    private LocalDate birth;
}
