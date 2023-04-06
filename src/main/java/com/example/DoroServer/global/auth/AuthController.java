package com.example.DoroServer.global.auth;

import com.example.DoroServer.domain.user.repository.UserRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "인증 🔐")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
}
