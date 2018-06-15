package com.vanderlei.cfp.email;

import com.vanderlei.cfp.email.templates.TemplateLancamentoVencido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public abstract class AbstractEmailService implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.default.sender}")
    private String sender;

    @Value("${mail.default.subject}")
    private String subject;

    @Override
    public void enviarEmailLancamentoVencido(final TemplateLancamentoVencido obj) {
        SimpleMailMessage simpleMailMessage = prepararSimpleMailMessageFromLancamento(obj);
        enviarEmail(simpleMailMessage);
    }

    @Override
    public void enviarEmailLancamentoVencidoHtml(final TemplateLancamentoVencido obj) {
        try {
            MimeMessage mimeMessage = prepararMimeMessageFromLancamento(obj);
            enviarEmail(mimeMessage);
        } catch (MessagingException e) {
            enviarEmailLancamentoVencido(obj);
        }
    }

    protected SimpleMailMessage prepararSimpleMailMessageFromLancamento(final TemplateLancamentoVencido obj) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(obj.getUsuario().getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setSentDate(new Date(System.currentTimeMillis()));
        simpleMailMessage.setText(obj.toString());
        return simpleMailMessage;
    }

    protected MimeMessage prepararMimeMessageFromLancamento(final TemplateLancamentoVencido obj) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(obj.getUsuario().getEmail());
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
        mimeMessageHelper.setText(htmlFromTemplatePedido(obj), true);

        return mimeMessage;
    }

    protected String htmlFromTemplatePedido(TemplateLancamentoVencido obj) {
        Context context = new Context();
        context.setVariable("lancamento", obj);
        return templateEngine.process("email/lancamentoVencido", context);
        }
}
