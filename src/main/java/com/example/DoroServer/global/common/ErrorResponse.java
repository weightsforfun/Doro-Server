package com.example.DoroServer.global.common;

import com.example.DoroServer.global.exception.Code;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;


@Getter
public class ErrorResponse extends BaseResponse {

    private ErrorResponse(Code errorCode) {
        super(false, errorCode.getCode(),errorCode.getMessage(),errorCode.getHttpStatus());
    }
    public static ErrorResponse errorResponse(Code errorCode){
        return new ErrorResponse(errorCode);
    }
}
