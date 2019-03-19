package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.CentroCustoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CentroCustoConverter implements Converter<CentroCustoDataContract, CentroCusto> {

  @Override
  public CentroCusto convert(final CentroCustoDataContract dataContract) {
    CentroCusto obj = new CentroCusto();
    BeanUtils.copyProperties(dataContract, obj);
    obj.setNome(obj.getNome().toUpperCase());
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
