package com.example.DoroServer.global.auth;

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
    @DisplayName("회원가입 - 관리자 회원가입 성공")
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
    @DisplayName("회원가입 - 유저 회원가입 성공")
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
    @DisplayName("회원가입 - 레디스 조회 실패 -> 전화번호 미인증")
    void JoinRedisException(){
        // given
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "1111");
        given(redisService.getValues(anyString())).willReturn(null);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("회원가입 - 휴대폰 번호 중복")
    void JoinDuplicateException(){
        // given
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "1111");

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(true);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.EXIST_PHONE);
    }

    @Test
    @DisplayName("회원가입 - 비밀번호, 비밀번호 확인 불일치")
    void JoinPasswordNotEqualException(){
        // given
        JoinReq joinReq = getJoinReq("password1", UserRole.ROLE_ADMIN, "1111");
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // when

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

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.DORO_ADMIN_AUTH_FAILED);
    }

    @Test
    @DisplayName("회원가입 - 유저 인증번호 불일치")
    void JoinUserAuthNumException(){
        // given
        ReflectionTestUtils.setField(authService, "DORO_USER", "2222");
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_USER, "1111");

        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        given(userRepository.existsByPhone(anyString())).willReturn(false);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.join(joinReq)).isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.DORO_USER_AUTH_FAILED);
    }

    @Test
    @DisplayName("아이디 중복 체크 - 성공")
    void checkAccountSuccess(){
        // given
        given(userRepository.existsByAccount(anyString())).willReturn(false);
        // when
        authService.checkAccount(anyString());
        // then
        verify(userRepository, times(1)).existsByAccount(anyString());
    }

    @Test
    @DisplayName("아이디 중복 체크 - 예외 발생")
    void checkAccountException(){
        // given
        given(userRepository.existsByAccount(anyString())).willReturn(true);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.checkAccount(anyString()))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.EXIST_ACCOUNT);
    }

    @Test
    @DisplayName("아이디 찾기 - 성공")
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
    @DisplayName("아이디 찾기 - 레디스 조회 실패 -> 전화번호 미인증")
    void findAccountRedisException(){
        // given
        given(redisService.getValues(anyString())).willReturn(null);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.findAccount(anyString()))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("아이디 찾기 - 계정 부재")
    void findAccountNotFound(){
        // given
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.findAccount(anyString()))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.ACCOUNT_NOT_FOUND);
        verify(userRepository, times(1)).findByPhone(anyString());
    }

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void changePasswordSuccess(){
        //given
        User user = setUpUser();
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq("changepassword1@");
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
    @DisplayName("비밀번호 변경 - 레디스 조회 실패 -> 전화번호 미인증")
    void changePasswordRedisException(){
        // given
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq("changePassword1@");
        given(redisService.getValues(anyString())).willReturn(null);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.changePassword(changePasswordReq))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.UNAUTHORIZED_PHONE_NUMBER);
    }

    @Test
    @DisplayName("비밀번호 변경 - 비밀번호, 비밀번호 확인 불일치")
    void changePasswordNotEqualException(){
        // given
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq("NotEqualPassword");
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        // when

        // then
        Assertions.assertThatThrownBy(() -> authService.changePassword(changePasswordReq))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.PASSWORD_DID_NOT_MATCH);
    }
    @Test
    @DisplayName("비밀번호 변경 - 계정 부재")
    void changePasswordAccountNotFound(){
        //given
        ChangePasswordReq changePasswordReq = setUpChangePasswordReq("changepassword1@");
        given(redisService.getValues(anyString())).willReturn(VERIFIED_CODE);
        //when

        //then
        Assertions.assertThatThrownBy(() -> authService.changePassword(changePasswordReq))
            .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.ACCOUNT_NOT_FOUND);
    }

    @Test
    @DisplayName("회원 탈퇴 - 성공")
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
    @DisplayName("회원 탈퇴 - 실패")
    public void testWithdrawalUserWithException() {
        // Given
        User user = setUpUser();
        doThrow(new RuntimeException()).when(userRepository).deleteById(any());

        // When

        // then
        Assertions.assertThatThrownBy(() -> authService.withdrawalUser(user))
                .isInstanceOf(BaseException.class)
            .hasFieldOrPropertyWithValue("code", Code.WITHDRAWAL_FAILED);

        then(userLectureRepository).should(times(1)).deleteAllByUser(user);
        then(userNotificationRepository).should(times(1)).deleteAllByUser(user);
        then(tokenRepository).should(times(1)).deleteAllByUser(user);
        then(userRepository).should(times(1)).deleteById(user.getId());
    }

    private ChangePasswordReq setUpChangePasswordReq(String passwordCheck) {
        return ChangePasswordReq.builder()
            .account("account")
            .phone("01011111111")
            .newPassword("changepassword1@")
            .newPasswordCheck(passwordCheck)
            .build();
    }


    private JoinReq getJoinReq(String passwordCheck, UserRole role, String authNum) {
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

    private User setUpUser(){
        return User.builder()
            .id(1L)
            .account("account")
            .password("password01@")
            .name("userName")
            .birth(LocalDate.of(2023, 7, 11))
            .gender(Gender.MALE)
            .phone("01011111111")
            .degree(
                Degree.builder()
                    .school("school")
                    .studentId("2023045650")
                    .major("major")
                    .studentStatus(StudentStatus.ATTENDING)
                    .build()
            )
            .generation(1)
            .role(UserRole.ROLE_USER)
            .profileImg("path/to/profile/test.jpg")
            .notificationAgreement(true)
            .isActive(true)
            .build();
    }
}