package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Baixa;
import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.BaixaDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BaixaConverter implements Converter<BaixaDataContract, Baixa> {

    @Override
    public Baixa convert(final BaixaDataContract dataContract) {
        Baixa obj = new Baixa();
        BeanUtils.copyProperties(dataContract, obj);
        if (dataContract.getContaBancaria() != null) {
            obj.setContaBancaria(new ContaBancaria(null, dataContract.getContaBancaria().getNome(), null, null, null,
                    null, null,
                    new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail())));
        }
        obj.setUsuario(new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail()));

        return obj;
    }
}