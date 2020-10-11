package com.zupchallenge.bank.resources;

import com.zupchallenge.bank.models.Account;
import com.zupchallenge.bank.models.JsonMessage;
import com.zupchallenge.bank.models.Logon;
import com.zupchallenge.bank.models.User;
import com.zupchallenge.bank.repository.AccountRepository;
import com.zupchallenge.bank.repository.UserRepository;
import com.zupchallenge.bank.services.auth.UserManagerService;
import com.zupchallenge.bank.services.token.TokenManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/v1/user")
public class UserResource {

    @Autowired
    TokenManagerService tokenManagerService;

    @Autowired
    UserManagerService userManagerService;

    @Autowired
    UserRepository ur;

    @PostMapping("logon")
    public ResponseEntity<JsonMessage> logon(@Valid @RequestBody Logon logon) {
        if (!tokenManagerService.isExpiredToken(logon.getCpf(), logon.getToken())) {
            tokenManagerService.revokeToken(logon.getCpf(), logon.getToken());
            User userByCpf = ur.findByCpf(logon.getCpf());
            if (userByCpf == null) {
                userManagerService.createUser(logon.getCpf(), logon.getPassword());
                return ResponseEntity.created(null).build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
