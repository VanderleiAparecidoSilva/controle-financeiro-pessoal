package com.vanderlei.cfp.config.profile;

import com.vanderlei.cfp.email.EmailService;
import com.vanderlei.cfp.email.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("oauth-security")
public class ProdConfig {

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }
}
