package com.zupchallenge.bank.models;

import com.zupchallenge.bank.validators.password.Password;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Logon {
    @NotBlank(message = "cpf is mandatory")
    @Pattern(regexp = "^[0-9]{11}$", message = "Malformed CPF")
    private String cpf;

    @NotBlank(message = "token is mandatory")
    @Pattern(regexp = "^[0-9]{6}$", message = "Malformed Token")
    private String token;

//    @Password
    private String password;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
