package com.zupchallenge.bank.models;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

public class CNH {
    @NotBlank(message = "cnh_front_url is mandatory")
    @URL(message = "Malformed URL")
    private String cnh_front_url;

    @NotBlank(message = "cnh_back_url is mandatory")
    @URL(message = "Malformed URL")
    private String cnh_back_url;

    public String getCnh_front_url() {
        return cnh_front_url;
    }
    public void setCnh_front_url(String cnh_front_url) {
        this.cnh_front_url = cnh_front_url;
    }
    public String getCnh_back_url() {
        return cnh_back_url;
    }
    public void setCnh_back_url(String cnh_back_url) {
        this.cnh_back_url = cnh_back_url;
    }
}
