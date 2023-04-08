package com.example.DoroServer.global.message;

import com.example.DoroServer.global.auth.dto.AuthRequestDto;

public interface MessageService {

    void sendAuthNum(AuthRequestDto.SendAuthNumDto sendAuthNumDto);
    void verifyAuthNum(AuthRequestDto.VerifyAuthNumDto verifyAuthNumDto);
}
