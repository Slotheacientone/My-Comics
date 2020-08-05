package com.group5.mycomics.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private JavaMailSender javaMailSender;

    public void sendEmail(SimpleMailMessage message){
        javaMailSender.send(message);
    }

}
