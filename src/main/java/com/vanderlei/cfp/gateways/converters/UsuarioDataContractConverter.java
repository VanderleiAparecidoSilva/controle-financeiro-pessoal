package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDataContractConverter implements Converter<Usuario, UsuarioDataContract> {

    @Override
    public UsuarioDataContract convert(final Usuario obj) {
        UsuarioDataContract dataContract = new UsuarioDataContract();
        BeanUtils.copyProperties(obj, dataContract);

        return dataContract;
    }
}