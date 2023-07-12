package com.example.DoroServer.global.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordReq {

    @NotBlank
    private String account;

    @NotBlank
    private String phone;

    @NotBlank
    @Pattern(regexp = "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{8,20})$",
        message = "영문, 숫자, 특수문자 포함 8~20자로 입력해주세요.")
    private String newPassword;

    @NotBlank
    private String newPasswordCheck;
}
