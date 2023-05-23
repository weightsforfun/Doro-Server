package com.example.DoroServer.global.message;

import com.example.DoroServer.global.message.dto.SendAuthNumReq;
import com.example.DoroServer.global.message.dto.VerifyAuthNumReq;

public interface MessageService {

    void sendAuthNum(SendAuthNumReq sendAuthNumReq);
    void verifyAuthNum(VerifyAuthNumReq verifyAuthNumReq);
}
