package com.example.DoroServer.global.jwt;

import static com.example.DoroServer.global.jwt.JwtAuthenticationEntryPoint.buildAuthErrorResponse;

import com.example.DoroServer.global.common.AuthErrorResponse;
import com.example.DoroServer.global.exception.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        Code errorCode = Code.FORBIDDEN;
        response.setStatus(errorCode.getHttpStatus().value());

        AuthErrorResponse authErrorResponse = buildAuthErrorResponse(errorCode);
        PrintWriter writer = response.getWriter();
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
}
