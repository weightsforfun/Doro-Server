package com.example.DoroServer.global.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ChangePasswordReq {

    @NotBlank
    private String account;

    @NotBlank
    private String phone;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$", message = "영문, 숫자 포함 8자 이상으로 입력해주세요.")
    private String newPassword;

    @NotBlank
    private String newPasswordCheck;
}
