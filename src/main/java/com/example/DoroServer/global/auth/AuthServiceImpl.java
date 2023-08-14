package com.example.DoroServer.global.auth;

import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.ACCOUNT;
import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.JOIN;
import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.PASSWORD;
import static com.example.DoroServer.global.common.Constants.VERIFIED_CODE;

import com.example.DoroServer.domain.token.repository.TokenRepository;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userLecture.repository.UserLectureRepository;
import com.example.DoroServer.domain.userNotification.repository.UserNotificationRepository;
import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserLectureRepository userLectureRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final TokenRepository tokenRepository;
    private final RedisService redisService;
    private final String DORO_ADMIN;
    private final String DORO_USER;


    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                            UserRepository userRepository,
                            UserLectureRepository userLectureRepository,
                            UserNotificationRepository userNotificationRepository,
                            TokenRepository tokenRepository,
                            RedisService redisService,
                            @Value("${doro.admin}") String doro_admin,
                            @Value("${doro.user}") String doro_user) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userLectureRepository = userLectureRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.tokenRepository = tokenRepository;
        this.redisService = redisService;
        DORO_ADMIN = doro_admin;
        DORO_USER = doro_user;
    }

    private void validatePhoneRedis(REDIS_MESSAGE_PREFIX prefix, String phone) {
        if(!VERIFIED_CODE.equals(redisService.getValues(prefix + phone))) {
            throw new BaseException(Code.UNAUTHORIZED_PHONE_NUMBER);
        }
    }

    private void validatePasswordConsistency(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)){
            throw new BaseException(Code.PASSWORD_DID_NOT_MATCH);
        }
    }

    @Override
    public void join(JoinReq joinReq) {
        validatePhoneRedis(JOIN, joinReq.getPhone());
        // 휴대폰 번호 중복 체크
        checkPhoneNumber(joinReq.getPhone());

        checkAccount(joinReq.getAccount());
        // 비밀번호, 비밀번호 확인 비교
        validatePasswordConsistency(joinReq.getPassword(), joinReq.getPasswordCheck());

        // Role - Admin 회원가입 희망 시 Admin Code 입력해야 가입 가능
        UserRole role = joinReq.getRole();
        String doroAuth = joinReq.getDoroAuth();
        if(role == UserRole.ROLE_ADMIN && !DORO_ADMIN.equals(doroAuth)) {
            throw new BaseException(Code.DORO_ADMIN_AUTH_FAILED);
        }
        else if(role == UserRole.ROLE_USER && !DORO_USER.equals(doroAuth)) {
            throw new BaseException(Code.DORO_USER_AUTH_FAILED);
        }
        User user = joinReq.toUserEntity();
        user.updatePassword(passwordEncoder.encode(joinReq.getPassword()));
        userRepository.save(user);
    }



    @Override
    public void checkAccount(String account) {
        if(userRepository.existsByAccount(account)){
            throw new BaseException(Code.EXIST_ACCOUNT);
        }
    }

    @Override
    public String findAccount(String phone) {
        validatePhoneRedis(ACCOUNT, phone);

        User user = userRepository.findByPhone(phone).orElseThrow(()
            -> new BaseException(Code.ACCOUNT_NOT_FOUND));
        return user.getAccount();
    }

    @Override
    public void changePassword(ChangePasswordReq changePasswordReq) {
        validatePhoneRedis(PASSWORD, changePasswordReq.getPhone());

        validatePasswordConsistency(changePasswordReq.getNewPassword(),
                changePasswordReq.getNewPasswordCheck());

        User user = userRepository.findByAccountAndPhone(changePasswordReq.getAccount(),
                changePasswordReq.getPhone())
            .orElseThrow(() -> new BaseException(Code.ACCOUNT_NOT_FOUND));
        user.updatePassword(passwordEncoder.encode(changePasswordReq.getNewPassword()));
    }

    @Override
    public void withdrawalUser(User user) {
        try {
            userLectureRepository.deleteAllByUser(user);
            userNotificationRepository.deleteAllByUser(user);
            tokenRepository.deleteAllByUser(user);
            userRepository.deleteById(user.getId());
        } catch (Exception e){
            throw new BaseException(Code.WITHDRAWAL_FAILED);
        }
    }

    @Override
    public void checkPhoneNumber(String phone) {
        if(userRepository.existsByPhone(phone)){
            throw new BaseException(Code.EXIST_PHONE);
        }
    }
}
