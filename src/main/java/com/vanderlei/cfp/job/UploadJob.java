package com.vanderlei.cfp.job;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.enums.TipoUpload;
import com.vanderlei.cfp.entities.upload.CentroCustoUpload;
import com.vanderlei.cfp.entities.upload.Upload;
import com.vanderlei.cfp.gateways.CentroCustoGateway;
import com.vanderlei.cfp.gateways.UploadGateway;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class UploadJob implements Job {

  @Autowired private UploadGateway uploadGateway;

  @Autowired private CentroCustoGateway centroCustoGateway;

  @Override
  public void execute(final JobExecutionContext jobExecutionContext) {
    log.info("Início da execução do job: {}", UploadJob.class.getSimpleName());

    Collection<Upload> uploads = uploadGateway.buscarUploadsPendentes();

    uploads.forEach(upload -> {
        switch (upload.getTipo()) {
            case CENTRO_CUSTO: {
                addCentroCusto(upload);
                setUploadProccess(upload);
            }

            case CONTA_BANCARIA: {

            }
        }
    });

    log.info("Término da execução do job: {}", UploadJob.class.getSimpleName());
  }

    private void setUploadProccess(final Upload upload) {
        upload.setProcessado(true);
        uploadGateway.save(upload);
    }

    private void addCentroCusto(final Upload upload) {
        CentroCusto obj = new CentroCusto();
        obj.setNome(((CentroCustoUpload)upload).getNome());
        obj.setAplicarNaReceita(((CentroCustoUpload)upload).getAplicarNaReceita());
        obj.setAplicarNaDespesa(((CentroCustoUpload)upload).getAplicarNaDespesa());
        obj.setUsuario(upload.getUsuario());

        centroCustoGateway.inserir(obj);
    }
}
