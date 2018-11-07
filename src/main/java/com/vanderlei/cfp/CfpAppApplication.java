package com.vanderlei.cfp;

import com.vanderlei.cfp.config.property.CfpApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CfpApiProperty.class)
public class CfpAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(CfpAppApplication.class, args);
  }
}
