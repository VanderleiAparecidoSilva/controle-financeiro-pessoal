package com.vanderlei.cfp.config.quartz;

import com.vanderlei.cfp.job.VencimentoJob;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
@ConditionalOnProperty(name = "quartz.config.enabled")
public class JobDetailConfig {

    @Bean
    @Qualifier("jobVencimento")
    public JobDetailFactoryBean jobVencimento() {
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