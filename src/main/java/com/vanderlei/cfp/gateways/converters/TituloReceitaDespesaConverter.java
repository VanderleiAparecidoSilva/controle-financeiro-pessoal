package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.TituloReceitaDespesa;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.TituloReceitaDespesaDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TituloReceitaDespesaConverter implements Converter<TituloReceitaDespesaDataContract, TituloReceitaDespesa> {

    @Override
    public TituloReceitaDespesa convert(final TituloReceitaDespesaDataContract dataContract) {
        TituloReceitaDespesa obj = new TituloReceitaDespesa();
        BeanUtils.copyProperties(dataContract, obj);
        obj.setUsuario(new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail()));

        return obj;
    }
}