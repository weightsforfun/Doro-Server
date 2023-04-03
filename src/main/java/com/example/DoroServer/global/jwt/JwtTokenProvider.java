package com.example.DoroServer.global.jwt;

import com.example.DoroServer.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
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
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
@PropertySource("classpath:/jwt.properties")
public class JwtTokenProvider {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private String secretKey;
    private String refreshSecretKey;
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
        this.refreshSecretKey = refreshSecretKey;
        this.accessTime = accessTime;
        this.refreshTime = refreshTime;
    }
    // 시크릿키 Base64 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    public String createToken(String account, Collection<? extends GrantedAuthority> roles, Integer tokenValidTime, String key) {
        Claims claims = Jwts.claims().setSubject(account); // sub: account 형태로 저장
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .setClaims(claims) //Payload에 Private Claim을 담기 위함
                .setIssuedAt(now) //발급시간
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String createAccessToken(String account, Collection<? extends GrantedAuthority> roles) {
        return this.createToken(account, roles, accessTime, secretKey);
    }

    public String createRefreshToken(String account, Collection<? extends GrantedAuthority> roles) {
        return this.createToken(account, roles, refreshTime, refreshSecretKey);
    }

    // 토큰에서 인증 정보 가져오기 - 권한 처리를 위함
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원 정보 추출
    private String getAccount(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰의 유효성, 만료일자 확인
    public boolean validateToken(ServletRequest servletRequest, String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            servletRequest.setAttribute("exception","MalformedJwtException");
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            servletRequest.setAttribute("exception","ExpiredJwtException");
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            servletRequest.setAttribute("exception","UnsupportedJwtException");
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            servletRequest.setAttribute("exception","IllegalArgumentException");
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}

