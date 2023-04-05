package com.example.DoroServer.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum Code {

    SUCCESS(OK,"SUCCESS","OK"),
    // Common
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "AUTH001", "잘못된 요청입니다."),
    // 인증 관련 오류 AUTH001,AUTH002...
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH001","접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH002","인증정보가 유효하지 않습니다."),
    JWT_BAD_REQUEST(HttpStatus.UNAUTHORIZED, "AUTH003","잘못된 JWT 서명입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH004","토큰이 만료되었습니다."),
    JWT_UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH006","지원하지 않는 JWT 토큰입니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH007","유효한 JWT 토큰이 없습니다.");
    //USER 관련 오류 U001,U002...

    //강의 관련 오류 L001,L002..

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
