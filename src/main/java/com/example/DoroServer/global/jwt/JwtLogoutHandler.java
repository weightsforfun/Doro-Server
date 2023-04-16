package com.example.DoroServer.global.jwt;

import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.exception.JwtAuthenticationException;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        String bearerAccessToken = request.getHeader("Authorization");
        String accessToken = bearerAccessToken.substring(7);

        if(!tokenProvider.validateToken(accessToken)){
            throw new JwtAuthenticationException(Code.BAD_REQUEST);
        }
        Authentication providerAuthentication = tokenProvider.getAuthentication(accessToken);
        log.info("Argument Authentication={}", providerAuthentication);
        if(redisService.getValues("RTK" + providerAuthentication.getName()) != null){
            redisService.deleteValues("RTK" + providerAuthentication.getName());
        }
        Long expiration = tokenProvider.getExpiration(accessToken);
        redisService.setValues(accessToken, "logout", Duration.ofMillis(expiration));
    }
}
