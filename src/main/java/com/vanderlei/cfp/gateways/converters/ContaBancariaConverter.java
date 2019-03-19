package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.ContaBancariaDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContaBancariaConverter implements Converter<ContaBancariaDataContract, ContaBancaria> {

  @Override
  public ContaBancaria convert(final ContaBancariaDataContract dataContract) {
    ContaBancaria obj = new ContaBancaria();
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
