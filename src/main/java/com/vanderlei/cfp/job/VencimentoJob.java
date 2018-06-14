package com.vanderlei.cfp.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class VencimentoJob implements Job {

    @Override
    public void execute(final JobExecutionContext jobExecutionContext) {
        log.info("Início da execução do job: {}", VencimentoJob.class.getSimpleName());

        log.info("Término da execução do job: {}", VencimentoJob.class.getSimpleName());
    }
}