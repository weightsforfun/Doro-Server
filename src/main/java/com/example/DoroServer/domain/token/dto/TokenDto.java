package com.example.DoroServer.domain.token.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    @NotBlank(message = "토큰이 필요합니다.")
    private String token;
}
