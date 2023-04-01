package com.example.DoroServer.global.common;

import com.example.DoroServer.global.exception.Code;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import static com.example.DoroServer.global.exception.Code.*;

@Getter
public class SuccessResponse<T> extends BaseResponse {

    private final T data;
    private SuccessResponse(T data){
        super(true, SUCCESS.getCode(),SUCCESS.getMessage(),SUCCESS.getHttpStatus());
        this.data=data;
    }
    public static<T> SuccessResponse<T> successResponse(T data){
        return new SuccessResponse<>(data);
    }

}
