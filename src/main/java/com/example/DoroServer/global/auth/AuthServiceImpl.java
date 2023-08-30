package com.example.DoroServer.global.auth;

import static com.example.DoroServer.global.common.Constants.AUTHORIZATION_HEADER;
import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.ACCOUNT;
import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.JOIN;
import static com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX.PASSWORD;
import static com.example.DoroServer.global.common.Constants.REDIS_REFRESH_TOKEN_PREFIX;
import static com.example.DoroServer.global.common.Constants.VERIFIED_CODE;

import com.example.DoroServer.domain.token.repository.TokenRepository;
import com.example.DoroServer.domain.token.service.TokenService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userLecture.repository.UserLectureRepository;
import com.example.DoroServer.domain.userNotification.repository.UserNotificationRepository;
import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.auth.dto.LoginReq;
import com.example.DoroServer.global.auth.dto.LoginRes;
import com.example.DoroServer.global.auth.dto.ReissueReq;
import com.example.DoroServer.global.common.Constants.REDIS_MESSAGE_PREFIX;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.exception.JwtAuthenticationException;
import com.example.DoroServer.global.jwt.JwtTokenProvider;
import com.example.DoroServer.global.jwt.RedisService;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;
    private final String DORO_ADMIN;
    private final String DORO_USER;


    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                            UserRepository userRepository,
                            UserLectureRepository userLectureRepository,
                            UserNotificationRepository userNotificationRepository,
                            TokenRepository tokenRepository,
                            RedisService redisService,
                            JwtTokenProvider tokenProvider,
                            AuthenticationManagerBuilder authenticationManagerBuilder,
                            TokenService tokenService,
                            @Value("${doro.admin}") String doro_admin,
                            @Value("${doro.user}") String doro_user) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userLectureRepository = userLectureRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.tokenRepository = tokenRepository;
        this.redisService = redisService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
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

    private String createAccessToken(UsernamePasswordAuthenticationToken authenticationToken) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("AccessToken 생성 준비 끝");
        return getAccessToken(authentication);
    }

    private String getAccessToken(Authentication authentication) {
        return tokenProvider.createAccessToken(
                authentication.getName(), ((User) authentication.getPrincipal()).getId(),
                authentication.getAuthorities());
    }

    @Override
    public void join(JoinReq joinReq) {
        validatePhoneRedis(JOIN, joinReq.getPhone());
        checkPhoneNumber(joinReq.getPhone());
        checkAccount(joinReq.getAccount());
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

    @Override
    public LoginRes login(LoginReq loginReq, String fcmToken, String userAgent) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginReq.getAccount(), loginReq.getPassword());

        String accessToken = createAccessToken(authenticationToken);
        String refreshToken = tokenProvider.createRefreshToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, accessToken);

        redisService.setValues(REDIS_REFRESH_TOKEN_PREFIX + loginReq.getAccount() + userAgent,
                refreshToken, Duration.ofDays(60));

        if(fcmToken != null) {
            Long userId = Long.valueOf(tokenProvider.getUserId(accessToken));
            tokenService.saveToken(userId, fcmToken);
        }

        return new LoginRes(httpHeaders, refreshToken);
    }

    @Override
    public HttpHeaders reissue(ReissueReq reissueReq, String userAgent) {
        tokenProvider.validateRefreshToken(reissueReq.getRefreshToken());
        Authentication authentication = tokenProvider.getAuthentication(
                reissueReq.getAccessToken().substring(7));
        String refreshToken = redisService.getValues(
                REDIS_REFRESH_TOKEN_PREFIX + authentication.getName() + userAgent);

        if (!reissueReq.getRefreshToken().equals(refreshToken)) {
            throw new JwtAuthenticationException(Code.REFRESH_TOKEN_DID_NOT_MATCH);
        }
        String newAccessToken = getAccessToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, newAccessToken);

        return httpHeaders;
    }

}
