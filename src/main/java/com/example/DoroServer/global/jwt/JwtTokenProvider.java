package com.example.DoroServer.global.jwt;

import static com.example.DoroServer.global.common.Constants.ACCESS_TOKEN_PREFIX;
import static com.example.DoroServer.global.common.Constants.AUTHORIZATION_HEADER;

import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;


@Component
@Slf4j
@PropertySource("classpath:/jwt.properties")
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private String secretKey;
    private final Integer accessTime;
    private final Integer refreshTime;

    public JwtTokenProvider(
            UserRepository userRepository,
            CustomUserDetailsService customUserDetailsService,
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.secret-refresh}") String refreshSecretKey,
            @Value("${jwt.access-token-seconds}") Integer accessTime,
            @Value("${jwt.refresh-token-seconds}") Integer refreshTime) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.secretKey = secretKey;
        this.accessTime = accessTime;
        this.refreshTime = refreshTime;
    }
    // 시크릿키 Base64 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public String createAccessToken(String account, long idx, Collection<? extends GrantedAuthority> roles) {
        Claims claims = Jwts.claims().setSubject(account); // sub: account 형태로 저장
        claims.put("id", idx);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
            .setHeaderParam("type","jwt")
            .setClaims(claims) //Payload에 Private Claim을 담기 위함
            .setIssuedAt(now) //발급시간
            .setExpiration(new Date(System.currentTimeMillis() + accessTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
            .setHeaderParam("type","jwt")
            .setIssuedAt(now) //발급시간
            .setExpiration(new Date(System.currentTimeMillis() + refreshTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    // 토큰에서 인증 정보 가져오기 - 권한 처리를 위함
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원 정보 추출
    private String getAccount(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getSubject();
        }catch (ExpiredJwtException e){
            log.info("AccessToken 만료 시 재발급 = {}", e.getClaims().toString());
            return e.getClaims().getSubject();
        }
    }

    public String getUserId(String token) {
        try {
            return String.valueOf(
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("id"));
        }catch (ExpiredJwtException e){
            log.info("AccessToken 만료 시 재발급 = {}", e.getClaims().toString());
            return e.getClaims().getSubject();
        }
    }

    // 토큰의 유효성, 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch(SecurityException | MalformedJwtException | IllegalArgumentException e){
            throw new JwtAuthenticationException(Code.JWT_BAD_REQUEST);
        }catch(ExpiredJwtException e){
            throw new JwtAuthenticationException(Code.JWT_TOKEN_EXPIRED);
        }catch(UnsupportedJwtException e){
            throw new JwtAuthenticationException(Code.JWT_UNSUPPORTED_TOKEN);
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(accessToken)
            .getBody()
            .getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}

