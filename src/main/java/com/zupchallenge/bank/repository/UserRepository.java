package com.zupchallenge.bank.repository;

import com.zupchallenge.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCpf(String cpf);
    User findByCpfAndPassword(String cpf, String password);
}
