package com.example.DoroServer.global.exception;

import com.example.DoroServer.global.common.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({BaseException.class})
    protected ErrorResponse handleBaseException(BaseException be){
        return ErrorResponse.errorResponse(be.getCode());
    }

}
