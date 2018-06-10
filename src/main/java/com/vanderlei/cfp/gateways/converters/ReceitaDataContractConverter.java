package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Receita;
import com.vanderlei.cfp.http.data.ReceitaDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReceitaDataContractConverter implements Converter<Receita, ReceitaDataContract> {

    @Override
    public ReceitaDataContract convert(Receita obj) {
        ReceitaDataContract objDataContract = new ReceitaDataContract();
        BeanUtils.copyProperties(obj, objDataContract);
        objDataContract.setUsuario(new UsuarioDataContract(null, obj.getUsuario().getNome(),
                obj.getUsuario().getEmail()));
        return objDataContract;
    }
}