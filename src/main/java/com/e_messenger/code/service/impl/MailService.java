package com.e_messenger.code.service.impl;

import com.e_messenger.code.controller.TestController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailService {
    JavaMailSender mailSender;

    @Async
    public void sendSimpleMail(TestController.MailRequest request){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(request.to());
        simpleMailMessage.setSubject(request.subject());
        simpleMailMessage.setText(String.valueOf((int) (Math.random() * 1000000)));

        mailSender.send(simpleMailMessage);
    }
}
