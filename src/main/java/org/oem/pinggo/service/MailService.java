package org.oem.pinggo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.web.authentication.preauth.j2ee.J2eeBasedPreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}") private String sender;
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendMail(SimpleMailMessage simpleMailMessage){
        javaMailSender.send(simpleMailMessage);
        log.info( "Mail Sent Successfully... to %s",simpleMailMessage.getTo().toString());


    }


    @Async
    public void sendMail(String to,String subject, String  message){


        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        // Setting up necessary details
        mailMessage.setFrom(sender);
        mailMessage.setTo(to);
        mailMessage.setText(message);
        mailMessage.setSubject(subject);
        // Sending the mail
        javaMailSender.send(mailMessage);
       log.info( "Mail Sent Successfully... to %s",mailMessage.getTo().toString());

    }

}
