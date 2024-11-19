package com.example.demo.user.infrastructure;

import com.example.demo.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final JavaMailSender javaMailSender;
    @Override
    public void send(String email, String title, String contents) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(contents);
        javaMailSender.send(message);
    }
}
