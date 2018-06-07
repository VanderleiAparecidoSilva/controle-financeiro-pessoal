package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConverter implements Converter<UsuarioDataContract, Usuario> {

    @Override
    public Usuario convert(final UsuarioDataContract dataContract) {
        Usuario obj = new Usuario();
        BeanUtils.copyProperties(dataContract, obj);

        return obj;
    }
}