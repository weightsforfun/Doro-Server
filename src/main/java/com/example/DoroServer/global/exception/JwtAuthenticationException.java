package com.example.DoroServer.global.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(Code code) {
        super(code.name());
    }
}
