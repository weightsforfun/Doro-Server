package com.example.DoroServer.global.auth;

import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;

public interface AuthService {

    void join(JoinReq joinReq);

    void checkAccount(String account);

    String findAccount(String phone);

    void changePassword(ChangePasswordReq changePasswordReq);
}
