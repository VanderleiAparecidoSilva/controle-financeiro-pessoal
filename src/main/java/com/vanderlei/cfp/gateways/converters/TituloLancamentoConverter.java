package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.TituloLancamento;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.TituloLancamentoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TituloLancamentoConverter implements Converter<TituloLancamentoDataContract, TituloLancamento> {

    @Override
    public TituloLancamento convert(final TituloLancamentoDataContract dataContract) {
        TituloLancamento obj = new TituloLancamento();
        BeanUtils.copyProperties(dataContract, obj);
        obj.setUsuario(new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail(),
                dataContract.getUsuario().getPermiteEmailLembrete()));

        return obj;
    }
}