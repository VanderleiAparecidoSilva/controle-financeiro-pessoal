package com.vanderlei.cfp.config.logging;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
public class ConfigurationLogging {

  @PostConstruct
  public void init() {
    log.info("==========================================");
    log.info("spring.application.name=" + name);
    log.info("spring.profiles.active=" + profile);
    log.info("==========================================");
  }

  @Value("${spring.application.name}")
  public String name;

  @Value("${spring.profiles.active}")
  public String profile;
}
