package com.zupchallenge.bank.models;

public class JsonMessage {
    private String message;

    public JsonMessage(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
