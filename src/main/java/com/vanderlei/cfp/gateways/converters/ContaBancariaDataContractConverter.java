package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.http.data.ContaBancariaDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContaBancariaDataContractConverter
    implements Converter<ContaBancaria, ContaBancariaDataContract> {

  @Override
  public ContaBancariaDataContract convert(ContaBancaria obj) {
    ContaBancariaDataContract objDataContract = new ContaBancariaDataContract();
    BeanUtils.copyProperties(obj, objDataContract);
    objDataContract.setNome(objDataContract.getNome().toUpperCase());
    objDataContract.setUsuario(
        new UsuarioDataContract(
            null,
            obj.getUsuario().getNome(),
            obj.getUsuario().getEmail(),
            null,
            obj.getUsuario().getPermiteEmailLembrete()));
    return objDataContract;
  }

  public Page<ContaBancariaDataContract> convert(final Page<ContaBancaria> objList) {
    return objList.map(obj -> this.convert(obj));
  }

  public List<ContaBancariaDataContract> convert(final List<ContaBancaria> objList) {
    return objList.stream().map(obj -> this.convert(obj)).collect(Collectors.toList());
  }
}
