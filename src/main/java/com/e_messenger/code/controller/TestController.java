package com.e_messenger.code.controller;

import com.e_messenger.code.service.impl.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {
    MailService mailService;

    public record MailRequest(String to, String subject) {}

    @PostMapping("/send")
    public void sendMail(@RequestBody MailRequest request){
        System.out.println("Received");
        mailService.sendSimpleMail(request);
        System.out.println("Succeed");
    }
}
