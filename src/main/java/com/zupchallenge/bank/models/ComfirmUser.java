package com.zupchallenge.bank.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ComfirmUser {
    @NotBlank(message = "email is mandatory")
    @Email
    private String email;

    @NotBlank(message = "cpf is mandatory")
    @Pattern(regexp = "^[0-9]{11}$", message = "Malformed CPF")
    private String cpf;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
