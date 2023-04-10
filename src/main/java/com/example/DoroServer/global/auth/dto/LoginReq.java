package com.example.DoroServer.global.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginReq {
    @NotBlank
    private String account;

    @NotBlank
    private String password;
}
