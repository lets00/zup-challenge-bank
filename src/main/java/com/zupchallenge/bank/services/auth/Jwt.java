package com.zupchallenge.bank.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class Jwt implements Serializable, JwtService {
    private static final long serialVersionUID = 1L;

    // Expiration time: 1 hour
    public static final long JWT_TOKEN_VALIDITY = 3600 * 1000;

    @Value("${jwt.secret}")
    private String SECRET;

    @Override
    public String getCpfOfToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        String encodedString = Base64.getEncoder().encodeToString(SECRET.getBytes());
        return Jwts.parser().setSigningKey(encodedString).parseClaimsJws(token).getBody();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    @Override
    public String generateToken(String info) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, info);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        String encodedString = Base64.getEncoder().encodeToString(SECRET.getBytes());
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, encodedString).compact();
    }

    @Override
    public Boolean validateToken(String token, String info) {
        final String username = getCpfOfToken(token);
        return (username.equals(info) && !isTokenExpired(token));
    }
}
