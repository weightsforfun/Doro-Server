package com.example.DoroServer.global.message;

import com.example.DoroServer.global.auth.dto.SendAuthNumReq;
import com.example.DoroServer.global.auth.dto.VerifyAuthNumReq;

public interface MessageService {

    void sendAuthNum(SendAuthNumReq sendAuthNumReq);
    void verifyAuthNum(VerifyAuthNumReq verifyAuthNumReq);
}
