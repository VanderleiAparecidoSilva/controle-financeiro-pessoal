package com.vanderlei.cfp.config.quartz;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

@Configuration
@ConditionalOnProperty(name = "quartz.config.enabled")
public class TriggerConfig {

    @Autowired
    private QuartzApplicationProperties quartzApplicationProperties;

    public static final String GROUP = "Controle-Financeiro-Pessoal-ExecutorJob";

    @Bean
    public CronTriggerFactoryBean cronVencimento(@Qualifier("jobVencimento") final JobDetail jobVencimento) {
        CronTriggerFactoryBean cron = new CronTriggerFactoryBean();
        cron.setJobDetail(jobVencimento);
        cron.setCronExpression(quartzApplicationProperties.getJob().getVencimento().getCronExpression());
        cron.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        cron.setName("vencimentoJob");
        cron.setGroup(GROUP);
        return cron;
    }

    @Bean
    public CronTriggerFactoryBean cronUpload(@Qualifier("jobUpload") final JobDetail jobUpload) {
        CronTriggerFactoryBean cron = new CronTriggerFactoryBean();
        cron.setJobDetail(jobUpload);
        cron.setCronExpression(quartzApplicationProperties.getJob().getUpload().getCronExpression());
        cron.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        cron.setName("uploadJob");
        cron.setGroup(GROUP);
        return cron;
    }
}