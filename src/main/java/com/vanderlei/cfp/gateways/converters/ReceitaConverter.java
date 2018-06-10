package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Receita;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.ReceitaDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReceitaConverter implements Converter<ReceitaDataContract, Receita> {

    @Override
    public Receita convert(final ReceitaDataContract dataContract) {
        Receita obj = new Receita();
        BeanUtils.copyProperties(dataContract, obj);
        obj.setUsuario(new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail()));

        return obj;
    }
}