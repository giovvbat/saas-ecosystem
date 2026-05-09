package com.giovanna.saas_notifications.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public boolean send(String destination, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(destination);
            message.setSubject(subject);
            message.setText(body);

            javaMailSender.send(message);
            
            return true;
        } catch (MailException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }
}
