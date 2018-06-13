package com.vanderlei.cfp.configs.profile;

import com.vanderlei.cfp.gateways.DBGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBGateway dbGateway;

    @Bean
    public boolean instantiateDatabase() {
        dbGateway.instantiateTestDatabase();

        return true;
    }
}