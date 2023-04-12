package com.example.DoroServer.global.common;

import com.example.DoroServer.global.exception.Code;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthErrorResponse {

    @Schema(defaultValue = "에러코드명")
    private Code errorCode;

    @Schema(defaultValue = "에러메시지")
    private String message;

    @Schema(defaultValue = "에러원인 클래스명")
    private String cause;

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}