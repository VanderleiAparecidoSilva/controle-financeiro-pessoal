package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.http.data.PermissaoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PermissaoConverter implements Converter<PermissaoDataContract, Permissao> {

  @Override
  public Permissao convert(final PermissaoDataContract dataContract) {
    Permissao obj = new Permissao();
    BeanUtils.copyProperties(dataContract, obj);

    return obj;
  }
}
