package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.http.data.CentroCustoDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CentroCustoDataContractConverter
    implements Converter<CentroCusto, CentroCustoDataContract> {

  @Override
  public CentroCustoDataContract convert(CentroCusto obj) {
    CentroCustoDataContract objDataContract = new CentroCustoDataContract();
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

  public Page<CentroCustoDataContract> convert(final Page<CentroCusto> objList) {
    return objList.map(obj -> this.convert(obj));
  }

  public List<CentroCustoDataContract> convert(final List<CentroCusto> objList) {
    return objList.stream().map(obj -> this.convert(obj)).collect(Collectors.toList());
  }
}
