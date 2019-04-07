package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.LancamentoFiltro;
import com.vanderlei.cfp.http.data.*;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LancamentoDataContractConverter
    implements Converter<Lancamento, LancamentoDataContract> {

  @Override
  public LancamentoDataContract convert(Lancamento obj) {
    LancamentoDataContract objDataContract = new LancamentoDataContract();
    BeanUtils.copyProperties(obj, objDataContract);
    objDataContract.setUsuario(
        new UsuarioDataContract(
            null,
            obj.getUsuario().getNome(),
            obj.getUsuario().getEmail(),
            obj.getUsuario().getEmailCC(),
            null,
            obj.getUsuario().getPermiteEmailLembrete()));

    objDataContract.setDescricao(obj.getNome().getNome());

    if (obj.getCentroCustoPrimario() != null) {
      objDataContract.setCentroCustoPrimario(
          new CentroCustoDataContract(
              null,
              obj.getCentroCustoPrimario().getNome(),
              obj.getCentroCustoPrimario().getPrimaria(),
              obj.getCentroCustoPrimario().getSecundaria(),
              obj.getCentroCustoPrimario().getAplicarNaDespesa(),
              obj.getCentroCustoPrimario().getAplicarNaReceita(),
              objDataContract.getUsuario()));
    }

    if (obj.getCentroCustoSecundario() != null) {
      objDataContract.setCentroCustoSecundario(
          new CentroCustoDataContract(
              null,
              obj.getCentroCustoSecundario().getNome(),
              obj.getCentroCustoSecundario().getPrimaria(),
              obj.getCentroCustoSecundario().getSecundaria(),
              obj.getCentroCustoSecundario().getAplicarNaDespesa(),
              obj.getCentroCustoSecundario().getAplicarNaReceita(),
              objDataContract.getUsuario()));
    }

    if (obj.getContaBancaria() != null) {
      objDataContract.setContaBancaria(
          new ContaBancariaDataContract(
              null,
              obj.getContaBancaria().getNome(),
              obj.getContaBancaria().getNumeroContaBancaria(),
              obj.getContaBancaria().getLimiteContaBancaria(),
              obj.getContaBancaria().getSaldoContaBancaria(),
              obj.getContaBancaria().getVincularSaldoBancarioNoTotalReceita(),
              obj.getContaBancaria().getAtualizarSaldoBancarioNaBaixaTitulo(),
              obj.getContaBancaria().getContaBancariaPadrao(),
              objDataContract.getUsuario()));
    }

    return objDataContract;
  }

  public Page<LancamentoDataContract> convert(final Page<Lancamento> objList) {
    return objList.map(obj -> this.convert(obj));
  }

  public List<LancamentoDataContract> convert(final List<Lancamento> objList) {
    return objList.stream().map(obj -> this.convert(obj)).collect(Collectors.toList());
  }
}
