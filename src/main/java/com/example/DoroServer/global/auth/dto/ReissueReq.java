package com.example.DoroServer.global.auth.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueReq {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
