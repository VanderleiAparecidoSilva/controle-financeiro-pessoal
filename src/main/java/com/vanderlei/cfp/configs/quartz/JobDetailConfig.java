package com.vanderlei.cfp.configs.quartz;

import com.netshoes.inventory.middleware.job.impl.*;
import com.vanderlei.cfp.job.impl.VencimentoJob;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
@ConditionalOnProperty(name = "quartz.config.enabled")
@Profile({"live", "dev", "integration"})
public class JobDetailConfig {

    @Bean
    public JobDetailFactoryBean jobReserveReturn() {
        return createJobDetail(VencimentoJob.class);
    }

    private static JobDetailFactoryBean createJobDetail(Class jobClass) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        factoryBean.setGroup(TriggerConfig.GROUP);
        return factoryBean;
    }
}