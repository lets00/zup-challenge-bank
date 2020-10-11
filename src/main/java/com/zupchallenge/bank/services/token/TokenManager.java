package com.zupchallenge.bank.services.token;

import com.zupchallenge.bank.models.Token;
import com.zupchallenge.bank.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class TokenManager implements TokenManagerService {
    @Autowired
    TokenRepository tr;

    @Override
    public void createToken(String cpf, String tokenValue) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        Date oneHourDate = cal.getTime();

        Token tokenManager = new Token();
        tokenManager.setCpf(cpf);
        tokenManager.setTokenValue(tokenValue);
        tokenManager.setExpirationDate(oneHourDate);
        tr.save(tokenManager);
    }

    @Override
    public boolean isExpiredToken(String cpf, String tokenValue) {
        Token token = tr.findByTokenValueAndCpf(tokenValue, cpf);
        if (token != null) {
            Date actualDate = new Date();
            return actualDate.after(token.getExpirationDate());
        }
        return true;
    }

    @Override
    public void revokeToken(String cpf, String tokenValue) {
        Token token = tr.findByTokenValueAndCpf(tokenValue, cpf);
        if (token != null) {
            tr.delete(token);
        }
    }
}
