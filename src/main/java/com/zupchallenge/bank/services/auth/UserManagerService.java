package com.zupchallenge.bank.services.auth;

public interface UserManagerService {
    public void createUser(String cpf, String password);
    public void updateUser(String cpf, String newPassword);
    public boolean verifyCredentials(String cpf, String password);
}
