package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.http.data.CentroCustoDataContract;
import com.vanderlei.cfp.http.data.ContaBancariaDataContract;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
            null,
            obj.getUsuario().getPermiteEmailLembrete()));

    objDataContract.setDescricao(obj.getNome().getNome());

    if (obj.getCentroCusto() != null) {
      objDataContract.setCentroCusto(
          new CentroCustoDataContract(
              null,
              obj.getCentroCusto().getNome(),
              obj.getCentroCusto().getAplicarNaDespesa(),
              obj.getCentroCusto().getAplicarNaReceita(),
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
              objDataContract.getUsuario()));
    }

    return objDataContract;
  }

  public Page<LancamentoDataContract> convert(final Page<Lancamento> objList) {
    return objList.map(obj -> this.convert(obj));
  }
}
