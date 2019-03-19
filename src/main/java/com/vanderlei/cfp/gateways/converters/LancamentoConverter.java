package com.vanderlei.cfp.gateways.converters;

import com.mysql.cj.util.StringUtils;
import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LancamentoConverter implements Converter<LancamentoDataContract, Lancamento> {

  @Override
  public Lancamento convert(final LancamentoDataContract dataContract) {
    Lancamento obj = new Lancamento();
    BeanUtils.copyProperties(dataContract, obj);
    if (!StringUtils.isNullOrEmpty(dataContract.getDescricao())) {
      obj.setNome(
          new TituloLancamento(
              null,
              dataContract.getDescricao().toUpperCase(),
              0,
              false,
              false,
              new Usuario(
                  null,
                  dataContract.getUsuario().getNome(),
                  dataContract.getUsuario().getEmail(),
                  dataContract.getUsuario().getEmailCC(),
                  dataContract.getUsuario().getPermiteEmailLembrete())));
    }
    if (dataContract.getCentroCustoPrimario() != null) {
      obj.setCentroCustoPrimario(
          new CentroCusto(
              null,
              dataContract.getCentroCustoPrimario().getNome().toUpperCase(),
              true,
              false,
              false,
              false,
              new Usuario(
                  null,
                  dataContract.getUsuario().getNome(),
                  dataContract.getCentroCustoPrimario().getUsuario().getEmail(),
                  dataContract.getCentroCustoPrimario().getUsuario().getEmailCC(),
                  dataContract.getUsuario().getPermiteEmailLembrete())));
    }
    if (dataContract.getCentroCustoSecundario() != null) {
      obj.setCentroCustoSecundario(
          new CentroCusto(
              null,
              dataContract.getCentroCustoSecundario().getNome().toUpperCase(),
              true,
              false,
              false,
              false,
              new Usuario(
                  null,
                  dataContract.getUsuario().getNome(),
                  dataContract.getCentroCustoSecundario().getUsuario().getEmail(),
                  dataContract.getCentroCustoSecundario().getUsuario().getEmailCC(),
                  dataContract.getUsuario().getPermiteEmailLembrete())));
    }
    if (dataContract.getContaBancaria() != null) {
      obj.setContaBancaria(
          new ContaBancaria(
              null,
              dataContract.getContaBancaria().getNome().toUpperCase(),
              null,
              null,
              null,
              null,
              null,
              new Usuario(
                  null,
                  dataContract.getContaBancaria().getUsuario().getNome(),
                  dataContract.getContaBancaria().getUsuario().getEmail(),
                  dataContract.getContaBancaria().getUsuario().getEmailCC(),
                  dataContract.getUsuario().getPermiteEmailLembrete())));
    }
    if (dataContract.getBaixa() != null) {
      obj.setBaixa(
          new Baixa(
              LocalDateTime.now(),
              dataContract.getObservacao(),
              new ContaBancaria(
                  null,
                  dataContract.getContaBancaria().getNome(),
                  null,
                  null,
                  null,
                  null,
                  null,
                  new Usuario(
                      null,
                      dataContract.getUsuario().getNome(),
                      dataContract.getUsuario().getEmail(),
                      dataContract.getUsuario().getEmailCC(),
                      dataContract.getUsuario().getPermiteEmailLembrete())),
              new Usuario(
                  null,
                  dataContract.getUsuario().getNome(),
                  dataContract.getUsuario().getEmail(),
                  dataContract.getUsuario().getEmailCC(),
                  dataContract.getUsuario().getPermiteEmailLembrete())));
    }
    obj.setUsuario(
        new Usuario(
            null,
            dataContract.getUsuario().getNome(),
            dataContract.getUsuario().getEmail(),
            dataContract.getUsuario().getEmailCC(),
            dataContract.getUsuario().getPermiteEmailLembrete()));

    return obj;
  }
}
