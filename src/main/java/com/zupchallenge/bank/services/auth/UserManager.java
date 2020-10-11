package com.zupchallenge.bank.services.auth;

import com.zupchallenge.bank.models.User;
import com.zupchallenge.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManager implements UserManagerService {

    @Autowired
    UserRepository ur;

    private String encodePassword(String password) {
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        return passEncoder.encode(password);
    }

    @Override
    public void createUser(String cpf, String password) {
        User newUser = new User();
        newUser.setCpf(cpf);
        newUser.setPassword(encodePassword(password));
        ur.save(newUser);
    }

    @Override
    public boolean verifyCredentials(String cpf, String password) {
        User verifiedUser = ur.findByCpfAndPassword(cpf, encodePassword(password));
        return verifiedUser != null;
    }

    @Override
    public void updateUser(String cpf, String newPassword) {
        User user = ur.findByCpf(cpf);
        if (user != null) {
            user.setPassword(encodePassword(newPassword));
            ur.save(user);
        }
    }
}
