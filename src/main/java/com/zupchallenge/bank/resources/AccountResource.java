package com.zupchallenge.bank.resources;

import com.zupchallenge.bank.models.Account;
import com.zupchallenge.bank.models.Client;
import com.zupchallenge.bank.models.JsonMessage;
import com.zupchallenge.bank.repository.AccountRepository;
import com.zupchallenge.bank.repository.ClientRepository;
import com.zupchallenge.bank.services.auth.JwtService;
import com.zupchallenge.bank.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(value="/v1/account")
public class AccountResource {
    @Value("${app.bankcode}")
    private String CODE;

    @Autowired
    AccountRepository ar;

    @Autowired
    ClientRepository cr;

    @Autowired
    JwtService token;

    @Autowired
    private EmailService emailService;

    private String generateNumber(int qtd) {
        Random random = new Random();
        String generatedNumber = "";
        for (int x = 0; x < qtd; x++){
            generatedNumber = String.format("%s%d", generatedNumber, random.nextInt(10));
        }
        return generatedNumber;
    }

    private Account createAccount(String cpf) {
        Account newAccout = new Account();
        newAccout.setAgency(generateNumber(4));
        newAccout.setAccount(generateNumber(8));
        newAccout.setCode(CODE);
        newAccout.setBalance(0);
        newAccout.setProposeCpf(cpf);
        ar.save(newAccout);
        return newAccout;
    }

    private void sendEmail(Account account, String email) {
        String message = String.format("Agency: %s\nAccount: %s\nCode: %s\nBalance: %f",
                account.getAgency(),
                account.getAccount(),
                account.getCode(),
                account.getBalance());
        emailService.sendEmail(email, "New Account created", message);
    }

    @GetMapping("/create")
    public ResponseEntity<JsonMessage> requestAccount(@RequestParam("token") String jwt) {
        if (!token.isTokenExpired(jwt)) {
            String cpf = token.getCpfOfToken(jwt);
            Account accountByCpf = ar.findByProposeCpf(cpf);
            if (accountByCpf == null) {
                Client clientByCpf = cr.findByCpf(cpf);
                Account newAccount = createAccount(cpf);
                sendEmail(newAccount, clientByCpf.getEmail());
                JsonMessage message = new JsonMessage("Account Created. Please view your email: " + clientByCpf.getEmail());
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.unprocessableEntity().build();
            }
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
