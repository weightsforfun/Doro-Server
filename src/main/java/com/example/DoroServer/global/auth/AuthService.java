package com.example.DoroServer.global.auth;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.auth.dto.LoginReq;
import com.example.DoroServer.global.auth.dto.ReissueReq;
import com.mysema.commons.lang.Pair;
import org.springframework.http.HttpHeaders;

public interface AuthService {

    void join(JoinReq joinReq);

    void checkAccount(String account);

    String findAccount(String phone);

    void changePassword(ChangePasswordReq changePasswordReq);

    void withdrawalUser(User user);

    void checkPhoneNumber(String phone);

    Pair<HttpHeaders, String> login(LoginReq loginReq, String fcmToken, String userAgent);

    HttpHeaders reissue(ReissueReq reissueReq, String userAgent);
}
