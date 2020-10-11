package com.zupchallenge.bank.resources;

import com.zupchallenge.bank.models.*;
import com.zupchallenge.bank.repository.AccountRepository;
import com.zupchallenge.bank.repository.ProposalRepository;
import com.zupchallenge.bank.services.auth.JwtService;
import com.zupchallenge.bank.services.email.EmailService;
import com.zupchallenge.bank.services.token.TokenManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Random;

@RestController
@RequestMapping(value="/v1/account")
public class AccountResource {
    @Value("${app.bankcode}")
    private String CODE;

    @Autowired
    AccountRepository ar;

    @Autowired
    ProposalRepository cr;

    @Autowired
    JwtService token;

    @Autowired
    TokenManagerService tokenManagerService;

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
                Proposal proposalByCpf = cr.findByCpf(cpf);
                Account newAccount = createAccount(cpf);
                sendEmail(newAccount, proposalByCpf.getEmail());
                JsonMessage message = new JsonMessage("Account Created. Please view your email: " + proposalByCpf.getEmail());
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.unprocessableEntity().build();
            }
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping("confirm")
    public ResponseEntity<JsonMessage> confirmIdentity(@Valid @RequestBody ComfirmUser confirmUser) {
        Proposal proposalByCpf = cr.findByCpf(confirmUser.getCpf());
        if (proposalByCpf != null) {
            if (proposalByCpf.getEmail().equals(confirmUser.getEmail())) {
                String token = generateNumber(6);
                tokenManagerService.createToken(confirmUser.getCpf(), token);
                emailService.sendEmail(confirmUser.getEmail(), "Your validate token", token);
                JsonMessage message = new JsonMessage("Validate token sent to email: " + proposalByCpf.getEmail());
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
