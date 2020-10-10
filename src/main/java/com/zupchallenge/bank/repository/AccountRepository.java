package com.zupchallenge.bank.repository;

import com.zupchallenge.bank.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>  {

    Account findByProposeCpf(String proposeCpf);

}
