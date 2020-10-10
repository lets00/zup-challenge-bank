package com.zupchallenge.bank.resources;

import com.zupchallenge.bank.auth.JWT;
import com.zupchallenge.bank.models.Account;
import com.zupchallenge.bank.models.Client;
import com.zupchallenge.bank.models.JsonMessage;
import com.zupchallenge.bank.repository.AccountRepository;
import com.zupchallenge.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(value="/v1/account")
public class AccountResource {
    private JWT token = new JWT();

    private String CODE = "001";

    @Autowired
    AccountRepository ar;

    @Autowired
    ClientRepository cr;

    @Autowired
    private JavaMailSender javaMailSender;

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
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("New Account created");
        String message = String.format("Agency: %s\nAccount: %s\nCode: %s\nBalance: %f",
                account.getAgency(),
                account.getAccount(),
                account.getCode(),
                account.getBalance());
        msg.setText(message);
        javaMailSender.send(msg);
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
