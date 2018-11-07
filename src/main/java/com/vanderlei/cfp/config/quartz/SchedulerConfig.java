package com.vanderlei.cfp.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Configuration
@ConditionalOnProperty(name = "quartz.config.enabled")
@Slf4j
public class SchedulerConfig {

    @Autowired
    private QuartzApplicationProperties quartzApplicationProperties;

    @Autowired
    private List<Trigger> listOfTrigger;

    @Bean
    @Primary
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSourceBean());
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(listOfTrigger.toArray(new Trigger[listOfTrigger.size()]));
        factory.setBeanName("SchedulerFactoryBean");
        factory.setSchedulerName("SchedulerControleFinanceiroPessoal");
        log.info("SchedulerControleFinanceiroPessoal created");
        return factory;
    }

    private DataSource dataSourceBean() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(quartzApplicationProperties.getDataSourceDriverClassName());
        dataSource.setUrl(quartzApplicationProperties.getDataSourceUrl() + "?useSSL=false");
        dataSource.setUsername(quartzApplicationProperties.getDataSourceUsername());
        dataSource.setPassword(quartzApplicationProperties.getDataSourcePassword());
        return dataSource;
    }

    private Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

}
