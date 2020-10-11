package com.zupchallenge.bank.repository;

import com.zupchallenge.bank.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByCpf(String cpf);
    Token findByTokenValueAndCpf(String token, String cpf);
}
