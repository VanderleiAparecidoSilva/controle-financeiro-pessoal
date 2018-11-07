package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.PermissaoUsuario;
import com.vanderlei.cfp.http.data.PermissaoUsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PermissaoUsuarioConverter
    implements Converter<PermissaoUsuarioDataContract, PermissaoUsuario> {

  @Override
  public PermissaoUsuario convert(final PermissaoUsuarioDataContract dataContract) {
    PermissaoUsuario obj = new PermissaoUsuario();
    BeanUtils.copyProperties(dataContract, obj);

    return obj;
  }
}
