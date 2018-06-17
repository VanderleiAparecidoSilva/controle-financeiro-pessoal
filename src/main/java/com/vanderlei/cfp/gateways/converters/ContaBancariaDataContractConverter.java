package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.http.data.ContaBancariaDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ContaBancariaDataContractConverter implements Converter<ContaBancaria, ContaBancariaDataContract> {

    @Override
    public ContaBancariaDataContract convert(ContaBancaria obj) {
        ContaBancariaDataContract objDataContract = new ContaBancariaDataContract();
        BeanUtils.copyProperties(obj, objDataContract);
        objDataContract.setUsuario(new UsuarioDataContract(null, obj.getUsuario().getNome(),
                obj.getUsuario().getEmail(), null, obj.getUsuario().getPermiteEmailLembrete()));
        return objDataContract;
    }
}