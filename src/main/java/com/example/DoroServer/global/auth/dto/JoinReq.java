package com.example.DoroServer.global.auth.dto;

import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class JoinReq {

    @NotBlank
    private String account;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "영문, 숫자 포함 8자 이상으로 입력해주세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "영문, 숫자 포함 8자 이상으로 입력해주세요.")
    private String passwordCheck;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birth;

    @NotNull
    private Gender gender;

    @NotBlank
    @Pattern(regexp = "^01([016789])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phone;

    @NotBlank
    private String school;

    @NotBlank
    private String studentId;

    @NotBlank
    private String major;

    @NotNull
    private StudentStatus studentStatus;

    @NotNull
    private int generation;

    @NotNull
    private UserRole role;

    @NotBlank
    private String doroAuth;

    private String profileImg;
    private Boolean notificationAgreement;

    public User toUserEntity() {
        return User.builder()
                .account(account)
                .password(password)
                .name(name)
                .birth(birth)
                .gender(gender)
                .phone(phone)
                .degree(
                        Degree.builder()
                                .school(school)
                                .studentId(studentId)
                                .major(major)
                                .studentStatus(studentStatus)
                                .build()
                )
                .generation(generation)
                .role(role)
                .profileImg(profileImg)
                .notificationAgreement(notificationAgreement)
                .build();
    }
}
