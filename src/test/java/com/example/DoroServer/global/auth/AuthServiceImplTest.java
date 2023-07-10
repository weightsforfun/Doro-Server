package com.example.DoroServer.global.auth;

import static com.example.DoroServer.global.common.Constants.VERIFIED_CODE;
import static org.junit.jupiter.api.Assertions.*;

import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    RedisService redisService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("관리자 회원가입 성공")
    void ShouldAdminJoinSuccessfully(){
        // given
        ReflectionTestUtils.setField(authService, "DORO_ADMIN", "1111");

        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "1111");

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("EncodedPassword");

        // when
        authService.join(joinReq);

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("유저 회원가입 성공")
    void ShouldUserJoinSuccessfully(){
        // given
        ReflectionTestUtils.setField(authService, "DORO_USER", "2222");

        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_USER, "2222");

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("EncodedPassword");

        // when
        authService.join(joinReq);

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("레디스 조회 실패 -> 전화번호 미인증")
    void JoinRedisException(){
        // given
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "1111");
        // when
        given(redisService.getValues(anyString())).willReturn(null);
        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("휴대폰 번호 중복")
    void JoinDuplicateException(){
        // given
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "1111");
        // when
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(true);
        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.EXIST_PHONE);
    }

    @Test
    @DisplayName("비밀번호, 비밀번호 확인 불일치")
    void JoinPasswordNotEqualException(){
        // given
        JoinReq joinReq = getJoinReq("password1", UserRole.ROLE_ADMIN, "1111");
        // when
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.PASSWORD_DID_NOT_MATCH);
    }

    @Test
    @DisplayName("회원가입 관리자 인증번호 불일치")
    void JoinAdminAuthNumException(){
        // given
        ReflectionTestUtils.setField(authService, "DORO_ADMIN", "1111");
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "2222");
        // when
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.DORO_ADMIN_AUTH_FAILED);
    }

    @Test
    @DisplayName("회원가입 유저 인증번호 불일치")
    void JoinUserAuthNumException(){
        // given
        ReflectionTestUtils.setField(authService, "DORO_USER", "2222");
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_USER, "1111");
        // when
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.DORO_USER_AUTH_FAILED);
    }


    private static JoinReq getJoinReq(String passwordCheck, UserRole role, String authNum) {
        return JoinReq.builder()
            .account("test")
            .password("password1@")
            .passwordCheck(passwordCheck)
            .name("name")
            .birth(LocalDate.parse("2023-07-10"))
            .gender(Gender.MALE)
            .phone("010-1111-1111")
            .school("school")
            .studentId("2023045650")
            .major("major")
            .studentStatus(StudentStatus.ATTENDING)
            .role(role)
            .doroAuth(authNum)
            .notificationAgreement(Boolean.TRUE)
            .build();
    }
}