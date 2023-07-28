package com.example.DoroServer.global.auth;

import static com.example.DoroServer.global.auth.AuthTestSetup.ACCESS_TOKEN;
import static com.example.DoroServer.global.auth.AuthTestSetup.ACCOUNT;
import static com.example.DoroServer.global.auth.AuthTestSetup.FCM_TOKEN;
import static com.example.DoroServer.global.auth.AuthTestSetup.FCM_TOKEN_HEADER;
import static com.example.DoroServer.global.auth.AuthTestSetup.PASSWORD;
import static com.example.DoroServer.global.auth.AuthTestSetup.REFRESH_TOKEN;
import static com.example.DoroServer.global.auth.AuthTestSetup.USER_AGENT;
import static com.example.DoroServer.global.auth.AuthTestSetup.USER_AGENT_HEADER;
import static com.example.DoroServer.global.auth.AuthTestSetup.getJoinReq;
import static com.example.DoroServer.global.auth.AuthTestSetup.setUpUser;
import static com.example.DoroServer.global.common.Constants.AUTHORIZATION_HEADER;
import static com.example.DoroServer.global.common.Constants.REDIS_REFRESH_TOKEN_PREFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.DoroServer.domain.token.service.TokenService;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.auth.dto.LoginReq;
import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.config.SecurityConfig;
import com.example.DoroServer.global.jwt.JwtTokenProvider;
import com.example.DoroServer.global.jwt.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private RedisService redisService;
    @Mock
    private TokenService tokenService;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = JsonMapper.builder().findAndAddModules().build();
    }

    @Test
    @DisplayName("join_성공")
    public void join() throws Exception {
        // Given
        JoinReq joinReq = getJoinReq("password1@", UserRole.ROLE_ADMIN, "1111");
        SuccessResponse<String> response = SuccessResponse.successResponse("회원가입 완료");
        // When

        // Then
        mockMvc.perform(post("/join")
                .content(objectMapper.writeValueAsString(joinReq))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value("회원가입 완료"));

        verify(authService, times(1)).checkAccount(anyString());
        verify(authService, times(1)).join(any(JoinReq.class));
    }

//    @Test
//    @DisplayName("login_성공")
//    public void login() throws Exception {
//        // Given
//        LoginReq loginReq = new LoginReq(ACCOUNT, PASSWORD);
//        User user = setUpUser();
//        String userId = String.valueOf(user.getId());
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getAccount(), loginReq.getPassword());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//
//        given(authenticationManagerBuilder.getObject().authenticate(authenticationToken)).willReturn(authentication);
//        given(tokenProvider.createAccessToken(authentication.getName(),
//                                            ((User)authentication.getPrincipal()).getId(),
//                                            authentication.getAuthorities()))
//                    .willReturn(ACCESS_TOKEN);
//        given(tokenProvider.createRefreshToken()).willReturn(REFRESH_TOKEN);
//        given(tokenProvider.getUserId(any())).willReturn(userId);
//
//        // When
//        ResultActions resultActions = mockMvc.perform(post("/login")
//            .content(objectMapper.writeValueAsString(loginReq))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header(USER_AGENT_HEADER, USER_AGENT)
//            .header(FCM_TOKEN_HEADER, FCM_TOKEN));
//
//        // Then
//        resultActions
//            .andExpect(status().isOk())
//            .andExpect(header().string(AUTHORIZATION_HEADER, ACCESS_TOKEN))
//            .andExpect(content().string(REFRESH_TOKEN));
//
//        then(redisService).should()
//            .setValues(REDIS_REFRESH_TOKEN_PREFIX + loginReq.getAccount() + USER_AGENT,
//                        REFRESH_TOKEN,
//                        any(Duration.class));
//        then(tokenService).should().saveToken(Long.valueOf(userId), FCM_TOKEN);
//    }
//
//    @Test
//    public void checkAccount() throws Exception {
//        // Given
//        String account = "username";
//        SuccessResponse<String> response = SuccessResponse.successResponse("사용 가능한 아이디입니다.");
//
//        // When
//        given(authService.checkAccount(account)).willReturn(response);
//
//        // Then
//        mockMvc.perform(get("/check/account")
//                .param("account", account)
//                .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.data").value("사용 가능한 아이디입니다."));
//    }

}