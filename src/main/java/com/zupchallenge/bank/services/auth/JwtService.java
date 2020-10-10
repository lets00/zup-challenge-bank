package com.zupchallenge.bank.services.auth;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    public String getCpfOfToken(String token);
    public Date getExpirationDateFromToken(String token);
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
    public Boolean isTokenExpired(String token);
    public String generateToken(String info);
    public Boolean validateToken(String token, String info);
}
