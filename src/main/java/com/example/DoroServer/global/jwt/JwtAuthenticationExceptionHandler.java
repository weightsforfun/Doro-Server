package com.example.DoroServer.global.jwt;

import static com.example.DoroServer.global.jwt.JwtAuthenticationEntryPoint.buildAuthErrorResponse;

import com.example.DoroServer.global.common.AuthErrorResponse;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.exception.JwtAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtAuthenticationExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (JwtAuthenticationException authException) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            PrintWriter writer = response.getWriter();
            String errorCodeName = authException.getMessage();
            Code errorCode = Code.valueOf(errorCodeName);

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
    }

}
