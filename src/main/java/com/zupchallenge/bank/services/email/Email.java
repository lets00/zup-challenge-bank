package com.zupchallenge.bank.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Email implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String email, String subject, String message) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(message);
        javaMailSender.send(msg);
    }
}
