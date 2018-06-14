package com.vanderlei.cfp.configs.quartz;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

@Configuration
@ConditionalOnProperty(name = "quartz.config.enabled")
@Profile({"live", "dev", "integration"})
public class TriggerConfig {

    @Autowired
    private QuartzApplicationProperties quartzApplicationProperties;

    public static final String GROUP = "Freedom-Backoffice-Inventory-Middleware";

    @Bean
    public CronTriggerFactoryBean cronReserveReturn(final JobDetail jobReserveReturn) {
        CronTriggerFactoryBean cron = new CronTriggerFactoryBean();
        cron.setJobDetail(jobReserveReturn);
        cron.setCronExpression(quartzApplicationProperties.getJob().getReserveReturn().getCronExpression());
        cron.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        cron.setName("reserveReturnJob");
        cron.setGroup(GROUP);
        return cron;
    }

    @Bean
    public CronTriggerFactoryBean cronReprocessReserveReturn(final JobDetail jobReprocessReserveReturn) {
        CronTriggerFactoryBean cron = new CronTriggerFactoryBean();
        cron.setJobDetail(jobReprocessReserveReturn);
        cron.setCronExpression(quartzApplicationProperties.getJob().getReprocessReserveReturn().getCronExpression());
        cron.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        cron.setName("reprocessReserveReturnJob");
        cron.setGroup(GROUP);
        return cron;
    }

    @Bean
    public CronTriggerFactoryBean cronInventoryMovementExecutionError(final JobDetail jobInventoryMovementExecutionError) {
        CronTriggerFactoryBean cron = new CronTriggerFactoryBean();
        cron.setJobDetail(jobInventoryMovementExecutionError);
        cron.setCronExpression(quartzApplicationProperties.getJob().getInventoryMovementExecutionError().getCronExpression());
        cron.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        cron.setName("InventoryMovementExecutionErrorJob");
        cron.setGroup(GROUP);
        return cron;
    }
}