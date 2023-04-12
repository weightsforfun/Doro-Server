package com.example.DoroServer.global.auth.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueReq {
    private String accessToken;
    private String refreshToken;
}
