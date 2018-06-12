package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Baixa;
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

        return obj;
    }
}