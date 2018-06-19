package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConverter implements Converter<UsuarioDataContract, Usuario> {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UsuarioConverter() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Usuario convert(final UsuarioDataContract dataContract) {
        Usuario obj = new Usuario();
        BeanUtils.copyProperties(dataContract, obj);
        obj.setSenha(passwordEncoder.encode(dataContract.getSenha()));
        return obj;
    }
}