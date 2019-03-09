package com.vanderlei.cfp.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Slf4j
public class MockEmailService extends AbstractEmailService {

    @Override
    public void enviarEmail(SimpleMailMessage msg) {
        log.info(LocalDateTime.now() + " - Simulando envio de email...");
        log.info(LocalDateTime.now() + " - " + msg.toString());
        log.info(LocalDateTime.now() + " - Email enviado");
    }

    @Override
    public void enviarEmail(MimeMessage msg) {
        log.info(LocalDateTime.now() + " - Simulando envio de email html...");
        log.info(LocalDateTime.now() + " - " + msg.toString());
        log.info(LocalDateTime.now() + " - Email enviado");
    }
}
