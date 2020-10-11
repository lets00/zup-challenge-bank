package com.zupchallenge.bank.services.token;

public interface TokenManagerService {
    public void createToken(String cpf, String tokenValue);
    public boolean isExpiredToken(String cpf, String tokenValue);
    public void revokeToken(String cpf, String tokenValue);
}
