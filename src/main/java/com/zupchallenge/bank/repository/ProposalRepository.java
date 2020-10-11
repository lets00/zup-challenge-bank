package com.zupchallenge.bank.repository;

import com.zupchallenge.bank.models.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
	Proposal findByCpf(String cpf);
	Proposal findByEmail(String email);
}
