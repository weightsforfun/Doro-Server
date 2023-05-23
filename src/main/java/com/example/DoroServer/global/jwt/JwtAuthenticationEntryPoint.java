package com.example.DoroServer.global.jwt;

import com.example.DoroServer.global.common.AuthErrorResponse;
import com.example.DoroServer.global.exception.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);
        PrintWriter writer = response.getWriter();
        Code errorCode = Code.UNAUTHORIZED;
        AuthErrorResponse authErrorResponse = buildAuthErrorResponse(errorCode);
        try{
            writer.write(authErrorResponse.toString());
        }catch(NullPointerException e){
            log.error("응답 메시지 작성 에러", e);
        }finally{
            if(writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
    // 리팩터링 필요 - static 메서드가 아닌 별개의 클래스에서 메서드 생성
    public static AuthErrorResponse buildAuthErrorResponse(Code errorCode) {
        return AuthErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getMessage())
                .cause(JwtAuthenticationFilter.class.getName())
                .build();
    }
}
