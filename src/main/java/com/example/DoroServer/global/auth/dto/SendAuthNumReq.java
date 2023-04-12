package com.example.DoroServer.global.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SendAuthNumReq {
    @NotBlank
    @Pattern(regexp = "^01([016789])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String phone;

    @NotNull
    private MessageType messageType;

    public enum MessageType{
        JOIN, ACCOUNT, PASSWORD, UPDATE
    }
}
