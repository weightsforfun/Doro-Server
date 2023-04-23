package com.example.DoroServer.domain.token.service;

import com.example.DoroServer.domain.token.dto.TokenDto;
import com.example.DoroServer.domain.token.entity.Token;
import com.example.DoroServer.domain.token.repository.TokenRepository;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    // userId를 통해 해당 user가 가지고 있는 토큰들 조회
    public List<TokenDto> findUserTokens(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("유저를 찾을 수 없습니다. id = {}", userId);
            throw new BaseException(Code.USER_NOT_FOUND);
        });
        return user.getTokens().stream()
                .map(Token::toDto)
                .collect(Collectors.toList());
    }

    // 저장되어있는 모든 토큰 조회
    public List<TokenDto> findAllTokens() {
        return tokenRepository.findAll().stream()
                .map(Token::toDto).collect(Collectors.toList());
    }

    // id에 해당하는 user에 token 추가
    @Transactional
    public Long saveToken(Long userId, TokenDto tokenDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("유저를 찾을 수 없습니다. id = {}", userId);
            throw new BaseException(Code.USER_NOT_FOUND);
        });
        Token token = Token.builder()
                .token(tokenDto.getToken())
                .build();
        user.addToken(token);
        return token.getId();
    }

    // 토큰 삭제 메소드
    @Transactional
    public void deleteToken(Long userId,TokenDto tokenDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("유저를 찾을 수 없습니다. id = {}", userId);
            throw new BaseException(Code.USER_NOT_FOUND);
        });
        user.getTokens().removeIf(token -> token.getToken().equals(tokenDto.getToken()));
    }
}
