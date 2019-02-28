package com.vanderlei.cfp.job;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.upload.CentroCustoUpload;
import com.vanderlei.cfp.entities.upload.ContaBancariaUpload;
import com.vanderlei.cfp.entities.upload.Upload;
import com.vanderlei.cfp.gateways.CentroCustoGateway;
import com.vanderlei.cfp.gateways.ContaBancariaGateway;
import com.vanderlei.cfp.gateways.UploadGateway;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class UploadJob implements Job {

  @Autowired private UploadGateway uploadGateway;

  @Autowired private CentroCustoGateway centroCustoGateway;

  @Autowired private ContaBancariaGateway contaBancariaGateway;

  @Override
  public void execute(final JobExecutionContext jobExecutionContext) {
    log.info("Início da execução do job: {}", UploadJob.class.getSimpleName());

    Collection<Upload> uploads = uploadGateway.buscarUploadsPendentes();

    uploads.forEach(
        upload -> {
          switch (upload.getTipo()) {
            case CENTRO_CUSTO:
              {
                addCentroCusto(upload);
                setUploadProccess(upload);
                break;
              }

            case CONTA_BANCARIA:
              {
                addContaBancaria(upload);
                setUploadProccess(upload);
                break;
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
    obj.setNome(((CentroCustoUpload) upload).getNome());
    obj.setPrimaria(((CentroCustoUpload) upload).getPrimaria());
    obj.setSecundaria(((CentroCustoUpload) upload).getSecundaria());
    obj.setAplicarNaReceita(((CentroCustoUpload) upload).getAplicarNaReceita());
    obj.setAplicarNaDespesa(((CentroCustoUpload) upload).getAplicarNaDespesa());
    obj.setUsuario(upload.getUsuario());

    Optional<CentroCusto> centroCusto =
        centroCustoGateway.buscarPorNomeUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail());
    if (!centroCusto.isPresent()) {
      centroCustoGateway.inserir(obj);
    }
  }

  private void addContaBancaria(final Upload upload) {
    ContaBancaria obj = new ContaBancaria();
    obj.setNome(((ContaBancariaUpload) upload).getNome());
    obj.setNumeroContaBancaria(((ContaBancariaUpload) upload).getNumeroContaBancaria());
    obj.setLimiteContaBancaria(((ContaBancariaUpload) upload).getLimiteContaBancaria());
    obj.setSaldoContaBancaria(((ContaBancariaUpload) upload).getSaldoContaBancaria());
    obj.setVincularSaldoBancarioNoTotalReceita(
        ((ContaBancariaUpload) upload).getVincularSaldoBancarioNoTotalReceita());
    obj.setAtualizarSaldoBancarioNaBaixaTitulo(
        ((ContaBancariaUpload) upload).getAtualizarSaldoBancarioNaBaixaTitulo());
    obj.setUsuario(upload.getUsuario());

    Optional<ContaBancaria> contaBancaria =
        contaBancariaGateway.buscarPorNomeUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail());
    if (!contaBancaria.isPresent()) {
      contaBancariaGateway.inserir(obj);
    }
  }
}
