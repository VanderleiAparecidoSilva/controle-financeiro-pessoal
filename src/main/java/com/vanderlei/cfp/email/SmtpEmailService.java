package com.vanderlei.cfp.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

@Slf4j
public class SmtpEmailService extends AbstractEmailService {

  @Autowired private MailSender mailSender;

  @Autowired private JavaMailSender javaMailSender;

  @Override
  public void enviarEmail(SimpleMailMessage msg) {
    log.info("Enviando e-mail...");
    mailSender.send(msg);
    log.info("Email enviado");
  }

  @Override
  public void enviarEmail(final MimeMessage msg) {
    log.info("Enviando e-mail...");
    javaMailSender.send(msg);
    log.info("Email enviado");
  }
}
