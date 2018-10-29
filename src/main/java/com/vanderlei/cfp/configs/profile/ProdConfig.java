package com.vanderlei.cfp.configs.profile;

import com.vanderlei.cfp.email.EmailService;
import com.vanderlei.cfp.email.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {

  @Bean
  public boolean instantiateDatabase() {
    return true;
  }

  @Bean
  public EmailService emailService() {
    return new SmtpEmailService();
  }
}
