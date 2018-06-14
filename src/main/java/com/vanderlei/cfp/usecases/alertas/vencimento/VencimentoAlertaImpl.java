package com.vanderlei.cfp.usecases.alertas.vencimento;

import com.vanderlei.cfp.job.impl.VencimentoJob;
import com.vanderlei.cfp.usecases.alertas.Alertas;
import com.vanderlei.cfp.usecases.alertas.VencimentoAlerta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VencimentoAlertaImpl implements VencimentoAlerta, Alertas {

    @Override
    public void execute() {
        log.debug("Executando o JOB de validação do Retorno da Reserva - FB-Inventory-Middleware");
        try {

        } catch (Exception e) {
            log.error("Erro na execução do job de vencimento!", e);
        }
        log.debug("Job de vencimento finalizado.");
    }

    @Override
    public Class getType() {
        return VencimentoJob.class;
    }
}