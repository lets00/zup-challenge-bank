package com.zupchallenge.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zupchallenge.bank.models.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	Client findByCpf(String cpf);
	Client findByEmail(String email);
}
