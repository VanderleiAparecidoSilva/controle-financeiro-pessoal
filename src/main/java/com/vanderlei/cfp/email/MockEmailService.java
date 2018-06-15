package com.vanderlei.cfp.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

@Slf4j
public class MockEmailService extends AbstractEmailService {

    @Override
    public void enviarEmail(SimpleMailMessage msg) {
        log.info("Simulando envio de email...");
        log.info(msg.toString());
        log.info("Email enviado");
    }

    @Override
    public void enviarEmail(MimeMessage msg) {
        log.info("Simulando envio de email html...");
        log.info(msg.toString());
        log.info("Email enviado");
    }
}
