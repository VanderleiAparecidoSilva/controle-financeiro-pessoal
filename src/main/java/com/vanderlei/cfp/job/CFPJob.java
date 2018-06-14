package com.vanderlei.cfp.job;

import com.vanderlei.cfp.usecases.alertas.Alertas;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public abstract class CFPJob implements Job {

    @Autowired
    private List<Alertas> alertasList;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        alertasList.stream()
                .filter(integration -> integration.getType().equals(this.getClass()))
                .forEach(integration -> {
                    try {
                        integration.execute();
                    } catch (Exception e) {
                        log.error("Erro na execução do job: {} - error: {}", integration.getClass().getSimpleName(), e.getMessage());
                    }
                });
    }
}