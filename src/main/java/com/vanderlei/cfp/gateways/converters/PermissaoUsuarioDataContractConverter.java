package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.PermissaoUsuario;
import com.vanderlei.cfp.http.data.PermissaoUsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PermissaoUsuarioDataContractConverter
    implements Converter<PermissaoUsuario, PermissaoUsuarioDataContract> {

  @Override
  public PermissaoUsuarioDataContract convert(PermissaoUsuario obj) {
    PermissaoUsuarioDataContract objDataContract = new PermissaoUsuarioDataContract();
    BeanUtils.copyProperties(obj, objDataContract);
    return objDataContract;
  }

  public Page<PermissaoUsuarioDataContract> convert(final Page<PermissaoUsuario> objList) {
    return objList.map(obj -> this.convert(obj));
  }
}
