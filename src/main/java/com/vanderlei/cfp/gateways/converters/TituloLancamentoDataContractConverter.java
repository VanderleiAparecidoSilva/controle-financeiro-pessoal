package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.TituloLancamento;
import com.vanderlei.cfp.http.data.TituloLancamentoDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TituloLancamentoDataContractConverter
    implements Converter<TituloLancamento, TituloLancamentoDataContract> {

  @Override
  public TituloLancamentoDataContract convert(TituloLancamento obj) {
    TituloLancamentoDataContract objDataContract = new TituloLancamentoDataContract();
    BeanUtils.copyProperties(obj, objDataContract);
    objDataContract.setUsuario(
        new UsuarioDataContract(
            null,
            obj.getUsuario().getNome(),
            obj.getUsuario().getEmail(),
            null,
            obj.getUsuario().getPermiteEmailLembrete()));
    return objDataContract;
  }

  public Page<TituloLancamentoDataContract> convert(final Page<TituloLancamento> objList) {
    return objList.map(obj -> this.convert(obj));
  }
}
