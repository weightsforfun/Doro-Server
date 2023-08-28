package com.example.DoroServer.global.auth;

import static com.example.DoroServer.global.common.Constants.REDIS_REFRESH_TOKEN_PREFIX;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.global.auth.dto.ChangePasswordReq;
import com.example.DoroServer.global.auth.dto.JoinReq;
import com.example.DoroServer.global.auth.dto.LoginReq;
import com.example.DoroServer.global.auth.dto.ReissueReq;
import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.jwt.JwtTokenProvider;
import com.example.DoroServer.global.jwt.RedisService;
import com.mysema.commons.lang.Pair;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "인증 🔐")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;

    @Operation(summary = "001_01", description = "회원가입")
    @PostMapping("/join")
    public SuccessResponse<String> join (@RequestBody @Valid JoinReq joinReq){
        authService.checkAccount(joinReq.getAccount());
        authService.join(joinReq);
        return SuccessResponse.successResponse("회원가입 완료");
    }


    @Operation(summary = "001_02", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody @Valid LoginReq loginReq,
                                    @RequestHeader(required = false) String fcmToken,
                                    @RequestHeader("User-Agent") String userAgent){
        Pair<HttpHeaders, String> result = authService.login(loginReq, fcmToken, userAgent);
        return ResponseEntity.ok()
            .headers(result.getFirst())
            .body(result.getSecond());
    }

    @Operation(summary = "001_03", description = "아이디 중복체크")
    @GetMapping("/check/account")
    public SuccessResponse<String> checkAccount(@RequestParam @NotBlank
            @Pattern(regexp = "^[A-Za-z0-9]{4,20}$", message = "영문, 숫자 포함 4~20자로 입력해주세요") String account){
        authService.checkAccount(account);
        return SuccessResponse.successResponse("사용 가능한 아이디입니다.");
    }

    @Operation(summary = "001_04", description = "아이디 찾기")
    @GetMapping("/find/account")
    public SuccessResponse<String> findAccount(@RequestParam
            @Pattern(regexp = "^01([016789])([0-9]{3,4})([0-9]{4})$", message = "올바른 휴대폰 번호 형식이 아닙니다.") String phone){
        String account = authService.findAccount(phone);
        return SuccessResponse.successResponse(account);
    }


    @Operation(summary = "001_05", description = "비밀번호 변경")
    @PostMapping("/change/password")
    public SuccessResponse<String> changePassword(@RequestBody @Valid ChangePasswordReq changePasswordReq){
        authService.changePassword(changePasswordReq);
        return SuccessResponse.successResponse("비밀번호가 변경되었습니다.");
    }

    @Operation(summary = "001_06", description = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody @Valid ReissueReq reissueReq,
                                    @RequestHeader("User-Agent") String userAgent){
        HttpHeaders result = authService.reissue(reissueReq, userAgent);
        return ResponseEntity.ok()
            .headers(result).build();
    }

    @Secured("ROLE_USER")
    @Operation(summary = "001_07", description = "회원 탈퇴")
    @DeleteMapping("/withdrawal")
    public SuccessResponse<String> withdrawalUser(@AuthenticationPrincipal User user,
                                            @RequestHeader("User-Agent") String userAgent){
        authService.withdrawalUser(user);
        if(redisService.getValues(
                REDIS_REFRESH_TOKEN_PREFIX + user.getAccount() + userAgent) != null){
            redisService.deleteValues(REDIS_REFRESH_TOKEN_PREFIX + user.getAccount() + userAgent);
        }
        SecurityContextHolder.clearContext();
        return SuccessResponse.successResponse("회원 탈퇴 성공");
    }

    @Operation(summary = "001_08", description = "휴대폰 번호 중복체크")
    @GetMapping("/check/phone")
    public SuccessResponse<String> checkPhoneNumber(
            @RequestParam @NotBlank @Pattern(regexp = "^01([016789])([0-9]{3,4})([0-9]{4})$",
                    message = "올바른 휴대폰 번호 형식이 아닙니다.") String phone){
        authService.checkPhoneNumber(phone);
        return SuccessResponse.successResponse("사용 가능한 번호입니다.");
    }




}
