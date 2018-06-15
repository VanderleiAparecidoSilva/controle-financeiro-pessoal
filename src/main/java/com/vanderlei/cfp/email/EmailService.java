package com.vanderlei.cfp.email;

import com.vanderlei.cfp.email.templates.TemplateLancamentoVencido;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.internet.MimeMessage;

public interface EmailService {

    void enviarEmail(final SimpleMailMessage msg);

    void enviarEmail(final MimeMessage msg);

    void enviarEmailLancamentoVencido(final TemplateLancamentoVencido obj);

    void enviarEmailLancamentoVencidoHtml(final TemplateLancamentoVencido obj);
}
