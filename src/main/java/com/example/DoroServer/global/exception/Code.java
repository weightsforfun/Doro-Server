package com.example.DoroServer.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum Code {

    SUCCESS(OK,"SUCCESS","OK"),
    //오류 등록 예시 (HTTP_STATUS,ERROR_CODE,MESSAGE)
    EXAMPLE_ERROR(BAD_REQUEST,"EX001","예외 코드 예시입니다."),
    EXAMPLE_ERROR2(REQUEST_TIMEOUT,"EX002","2번째 예외 코드 예시입니다.");
    // 인증 관련 오류 AUTH001,AUTH002...

    //USER 관련 오류 U001,U002...

    //강의 관련 오류 L001,L002..

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
