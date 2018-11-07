package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.http.data.PermissaoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PermissaoDataContractConverter implements Converter<Permissao, PermissaoDataContract> {

  @Override
  public PermissaoDataContract convert(Permissao obj) {
    PermissaoDataContract objDataContract = new PermissaoDataContract();
    BeanUtils.copyProperties(obj, objDataContract);
    return objDataContract;
  }

  public Page<PermissaoDataContract> convert(final Page<Permissao> objList) {
    return objList.map(obj -> this.convert(obj));
  }
}
