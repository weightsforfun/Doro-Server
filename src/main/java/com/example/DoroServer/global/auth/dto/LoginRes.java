package com.example.DoroServer.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;

@Data
@AllArgsConstructor
public class LoginRes {
    HttpHeaders accessTokenHeader;
    String refreshToken;
}
