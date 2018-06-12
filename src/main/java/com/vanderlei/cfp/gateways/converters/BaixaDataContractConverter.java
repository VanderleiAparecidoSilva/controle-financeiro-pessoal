package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Baixa;
import com.vanderlei.cfp.http.data.BaixaDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BaixaDataContractConverter implements Converter<Baixa, BaixaDataContract> {

    @Override
    public BaixaDataContract convert(final Baixa obj) {
        BaixaDataContract dataContract = new BaixaDataContract();
        BeanUtils.copyProperties(obj, dataContract);

        return dataContract;
    }
}