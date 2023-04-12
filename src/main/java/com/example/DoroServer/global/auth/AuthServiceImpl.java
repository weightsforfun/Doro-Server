package com.example.DoroServer.global.auth;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final String DORO_ADMIN;
    private final String DORO_USER;

    public AuthServiceImpl(UserRepository userRepository,
                            RedisService redisService,
                            @Value("${doro.admin}") String doro_admin,
                            @Value("${doro.user}") String doro_user) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        DORO_ADMIN = doro_admin;
        DORO_USER = doro_user;
    }

    @Transactional
    @Override
    public void join(JoinReq joinReq) {
        // 레디스 인증된 번호 조회
        if(!"Verified".equals(redisService.getValues(joinReq.getPhone()))) {
            throw new BaseException(Code.UNAUTHORIZED_PHONE_NUMBER);
        }
        // 비밀번호, 비밀번호 확인 비교
        if(!joinReq.getPassword().equals(joinReq.getPasswordCheck())){
            throw new BaseException(Code.PASSWORD_DID_NOT_MATCH);
        }

        // Role - Admin 회원가입 희망 시 Admin Code 입력해야 가입 가능
        UserRole role = joinReq.getRole();
        String doroAuth = joinReq.getDoroAuth();
        if(role == UserRole.ROLE_ADMIN && !DORO_ADMIN.equals(doroAuth)) {
            throw new BaseException(Code.DORO_ADMIN_AUTH_FAILED);
        }
        if(role == UserRole.ROLE_USER && !DORO_USER.equals(doroAuth)) {
            throw new BaseException(Code.DORO_USER_AUTH_FAILED);
        }
        User user = joinReq.toUserEntity();
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void checkAccount(String account) {
        if(userRepository.existsByAccount(account)){
            throw new BaseException(Code.EXIST_ACCOUNT);
        }
    }
}
