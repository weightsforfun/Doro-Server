package com.example.DoroServer.global.auth;

import static com.example.DoroServer.global.auth.AuthTestSetup.DORO_ADMIN;
import static com.example.DoroServer.global.auth.AuthTestSetup.DORO_ADMIN_CODE;
import static com.example.DoroServer.global.auth.AuthTestSetup.DORO_USER;
import static com.example.DoroServer.global.auth.AuthTestSetup.DORO_USER_CODE;
import static com.example.DoroServer.global.auth.AuthTestSetup.EXCEPTION_CODE_FIELD;
import static com.example.DoroServer.global.auth.AuthTestSetup.NEWPASSWORD;
import static com.example.DoroServer.global.auth.AuthTestSetup.PASSWORD_CHECK;
import static com.example.DoroServer.global.auth.AuthTestSetup.WRONG_PASSWORD;
import static com.example.DoroServer.global.auth.AuthTestSetup.getJoinReq;
import static com.example.DoroServer.global.auth.AuthTestSetup.setUpChangePasswordReq;
import static com.example.DoroServer.global.auth.AuthTestSetup.setUpUser;
import static com.example.DoroServer.global.common.Constants.VERIFIED_CODE;
import static org.junit.jupiter.api.Assertions.*;

import com.example.DoroServer.domain.token.repository.TokenRepository;
import com.example.DoroServer.domain.user.entity.Degree;
import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.StudentStatus;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.userLecture.repository.UserLectureRepository;
import com.example.DoroServer.domain.userNotification.repository.UserNotificationRepository;
import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.jwt.RedisService;
import java.time.LocalDate;
import java.util.Optional;
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
    @Mock
    UserLectureRepository userLectureRepository;
    @Mock
    UserNotificationRepository userNotificationRepository;
    @Mock
    TokenRepository tokenRepository;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("join_관리자 회원가입_성공")
    void ShouldAdminJoinSuccessfully(){
        // given
        ReflectionTestUtils.setField(authService, DORO_ADMIN, DORO_ADMIN_CODE);

        JoinReq joinReq = getJoinReq(PASSWORD_CHECK, UserRole.ROLE_ADMIN, DORO_ADMIN_CODE);

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn(anyString());

        // when
        authService.join(joinReq);

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("join_유저 회원가입_성공")
    void ShouldUserJoinSuccessfully(){
        // given
        ReflectionTestUtils.setField(authService, DORO_USER, DORO_USER_CODE);

        JoinReq joinReq = getJoinReq(PASSWORD_CHECK, UserRole.ROLE_USER, DORO_USER_CODE);

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn(anyString());

        // when
        authService.join(joinReq);

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("join_레디스 조회 실패_전화번호 미인증 예외")
    void JoinRedisException(){
        // given
        JoinReq joinReq = getJoinReq(PASSWORD_CHECK, UserRole.ROLE_ADMIN, DORO_ADMIN_CODE);
        given(redisService.getValues(anyString())).willReturn(null);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("join_휴대폰 번호 중복_예외")
    void JoinDuplicateException(){
        // given
        JoinReq joinReq = getJoinReq(PASSWORD_CHECK, UserRole.ROLE_ADMIN, DORO_ADMIN_CODE);

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(true);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.EXIST_PHONE);
    }

    @Test
    @DisplayName("join_비밀번호, 비밀번호 확인 불일치_예외")
    void JoinPasswordNotEqualException(){
        // given
        JoinReq joinReq = getJoinReq(WRONG_PASSWORD, UserRole.ROLE_ADMIN, DORO_ADMIN_CODE);
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.PASSWORD_DID_NOT_MATCH);
    }

    @Test
    @DisplayName("join_관리자 인증번호 불일치_예외")
    void JoinAdminAuthNumException(){
        // given
        ReflectionTestUtils.setField(authService, DORO_ADMIN, DORO_ADMIN_CODE);
        JoinReq joinReq = getJoinReq(PASSWORD_CHECK, UserRole.ROLE_ADMIN, DORO_USER_CODE);

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.DORO_ADMIN_AUTH_FAILED);
    }

    @Test
    @DisplayName("join_유저 인증번호 불일치_예외")
    void JoinUserAuthNumException(){
        // given
        ReflectionTestUtils.setField(authService, DORO_USER, DORO_USER_CODE);
        JoinReq joinReq = getJoinReq(PASSWORD_CHECK, UserRole.ROLE_USER, DORO_ADMIN_CODE);

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.DORO_USER_AUTH_FAILED);
    }

    @Test
    @DisplayName("checkAccount_성공")
    void checkAccountSuccess(){
        // given
        given(userRepository.existsByAccount(anyString())).willReturn(false);
        // when
        authService.checkAccount(anyString());
        // then
        verify(userRepository, times(1)).existsByAccount(anyString());
    }

    @Test
    @DisplayName("checkAccount_예외")
    void checkAccountException(){
        // given
        given(userRepository.existsByAccount(anyString())).willReturn(true);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.checkAccount(anyString()))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.EXIST_ACCOUNT);
    }

    @Test
    @DisplayName("findAccount_성공")
    void findAccountSuccess(){
        // given
        User user = setUpUser();
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.findByPhone(anyString())).willReturn(Optional.of(user));
        // when
        String result = authService.findAccount(user.getPhone());
        // then
        assertEquals(user.getAccount(), result);
    }

    @Test
    @DisplayName("findAccount_레디스 조회 실패_전화번호 미인증 예외")
    void findAccountRedisException(){
        // given
        given(redisService.getValues(anyString())).willReturn(null);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.findAccount(anyString()))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("findAccount_계정 부재_예외")
    void findAccountNotFound(){
        // given
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.findAccount(anyString()))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.ACCOUNT_NOT_FOUND);
        verify(userRepository, times(1)).findByPhone(anyString());
    }

    @Test
    @DisplayName("changePassword_성공")
    void changePasswordSuccess(){
        //given
        User user = setUpUser();
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq(NEWPASSWORD);
        String newPassword = changePasswordReq.getNewPassword();

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.findByAccountAndPhone(anyString(), anyString())).willReturn(
            Optional.of(user));
        given(passwordEncoder.encode(anyString())).willReturn(newPassword);
        //when
        authService.changePassword(changePasswordReq);
        //then
        Assertions.assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("changePassword_레디스 조회 실패_전화번호 미인증 예외")
    void changePasswordRedisException(){
        // given
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq(NEWPASSWORD);
        given(redisService.getValues(anyString())).willReturn(null);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.changePassword(changePasswordReq))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("changePassword_비밀번호, 비밀번호 확인 불일치_예외")
    void changePasswordNotEqualException(){
        // given
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq(WRONG_PASSWORD);
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.changePassword(changePasswordReq))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.PASSWORD_DID_NOT_MATCH);
    }
    @Test
    @DisplayName("changePassword_계정 부재_예외")
    void changePasswordAccountNotFound(){
        //given
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq(NEWPASSWORD);
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        //when

        //then
        Assertions.assertThatThrownBy(() -> authService.changePassword(changePasswordReq))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.ACCOUNT_NOT_FOUND);
    }

    @Test
    @DisplayName("withdrawalUser_성공")
    void withdrawalSucess(){
        // Given
        User user = setUpUser();
        // When
        authService.withdrawalUser(user);
        // Then
        verify(userLectureRepository, times(1)).deleteAllByUser(user);
        verify(userNotificationRepository, times(1)).deleteAllByUser(user);
        verify(tokenRepository, times(1)).deleteAllByUser(user);
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    @DisplayName("withdrawalUser_예외")
    public void testWithdrawalUserWithException() {
        // Given
        User user = setUpUser();
        doThrow(new RuntimeException()).when(userRepository).deleteById(any());

        // When

        // then
        Assertions.assertThatThrownBy(() -> authService.withdrawalUser(user))
                .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue(EXCEPTION_CODE_FIELD, Code.WITHDRAWAL_FAILED);

        then(userLectureRepository).should(times(1)).deleteAllByUser(user);
        then(userNotificationRepository).should(times(1)).deleteAllByUser(user);
        then(tokenRepository).should(times(1)).deleteAllByUser(user);
        then(userRepository).should(times(1)).deleteById(user.getId());
    }


}