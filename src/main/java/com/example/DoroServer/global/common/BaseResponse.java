package com.example.DoroServer.global.common;


import com.example.DoroServer.global.exception.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public class BaseResponse {
    private final Boolean success;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public static BaseResponse baseResponse(Boolean success, Code code){
        return new BaseResponse(success,code.getCode(),code.getMessage(),code.getHttpStatus());
    }
}
