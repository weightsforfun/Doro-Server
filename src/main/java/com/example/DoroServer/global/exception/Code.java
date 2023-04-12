package com.example.DoroServer.global.exception;

import com.google.api.Http;
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
    JWT_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH007","유효한 JWT 토큰이 없습니다."),
    UNAUTHORIZED_PHONE_NUMBER(HttpStatus.UNAUTHORIZED, "AUTH008", "인증되지 않은 전화번호입니다."),
    EXIST_ACCOUNT(HttpStatus.CONFLICT, "AUTH009", "이미 존재하는 아이디입니다."),
    PASSWORD_DID_NOT_MATCH(HttpStatus.BAD_REQUEST, "AUTH010", "비밀번호가 일치하지 않습니다."),
    DORO_ADMIN_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "AUTH011", "관리자 인증번호가 일치하지 않습니다."),
    DORO_USER_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "AUTH012", "도로 인증번호가 일치하지 않습니다."),
    //USER 관련 오류 U001,U002...

    //강의 관련 오류 L001,L002..

    // 알림톡 관련 오류 M001, M002...
    MESSAGE_SEND_FAILED(HttpStatus.BAD_REQUEST, "M001", "메시지 전송이 실패했습니다. 올바른 번호인지 확인하세요."),
    VERIFICATION_DID_NOT_MATCH(HttpStatus.BAD_REQUEST, "M002", "인증 번호가 일치하지 않습니다."),

    // FCM 푸쉬 관련 오류
    NOTIFICATION_PUSH_FAIL(HttpStatus.BAD_REQUEST,"NOTI001","FCM 알림 푸쉬에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
