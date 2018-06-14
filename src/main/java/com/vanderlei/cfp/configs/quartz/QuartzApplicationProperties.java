package com.vanderlei.cfp.configs.quartz;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "quartz.config")
@Component
@Profile({"live", "dev", "integration"})
public class QuartzApplicationProperties {

    private String dataSourceUrl;

    private String dataSourceUsername;

    private String dataSourcePassword;

    private String dataSourceDriverClassName;

    private JobConfig job;

    private boolean enabled;

    @Getter
    @Setter
    public static class JobConfig{

        private CronExpression vencimento;

        @Getter
        @Setter
        public static class CronExpression{
            private String cronExpression;
        }
    }

}