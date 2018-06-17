package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.http.data.CentroCustoDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CentroCustoDataContractConverter implements Converter<CentroCusto, CentroCustoDataContract> {

    @Override
    public CentroCustoDataContract convert(CentroCusto obj) {
        CentroCustoDataContract objDataContract = new CentroCustoDataContract();
        BeanUtils.copyProperties(obj, objDataContract);
        objDataContract.setUsuario(new UsuarioDataContract(null, obj.getUsuario().getNome(),
                obj.getUsuario().getEmail(), null, obj.getUsuario().getPermiteEmailLembrete()));
        return objDataContract;
    }
}
