package com.ptit.news.service;

import com.ptit.news.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;
    private static final String SECRET_KEY = "8ed09dfea89dd8afe6c430c2c0c18bddee1f31d947b11049bbb1c440c0b5b7f6";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Phương thức chính để tạo JWT token
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Boolean isRemember) {
        Date dateExpiration;
        if (isRemember) {
            dateExpiration = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30); // 30 ngày
        } else {
            dateExpiration = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24); // 24 giờ
        }

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(dateExpiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Phương thức này sẽ được gọi từ SignInCommandHandler
    public String generateToken(UserDetails userDetails, Boolean isRemember) {
        // Tạo một HashMap để chứa các claims
        Map<String, Object> claims = new HashMap<>();

        // **Phần quan trọng:** Lấy vai trò (authorities) từ UserDetails
        // và thêm vào claims.
        // Spring Security lưu trữ vai trò trong Authorities dưới dạng GrantedAuthority.
        // Bạn cần map chúng thành List<String> để đưa vào token.
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));

        // Gọi phương thức tạo token với claims đã có vai trò
        return generateToken(claims, userDetails, isRemember);
    }

    // ... các phương thức khác giữ nguyên

    public String generateTokenForgotPassword(String email) {
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolve) {
        final Claims claims = extractAllClaims(token);
        return claimsResolve.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parse(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetail) {
        final String email = extractUsername(token);
        boolean isValid = tokenRepository.findByCode(token).map(t -> !t.getIsSignOut()).orElse(false);
        return isValid && email.equals(userDetail.getUsername()) && !isTokenExpired(token);
    }

}