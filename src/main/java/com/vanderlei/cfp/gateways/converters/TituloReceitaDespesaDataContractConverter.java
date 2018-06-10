package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.TituloReceitaDespesa;
import com.vanderlei.cfp.http.data.TituloReceitaDespesaDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TituloReceitaDespesaDataContractConverter implements Converter<TituloReceitaDespesa, TituloReceitaDespesaDataContract> {

    @Override
    public TituloReceitaDespesaDataContract convert(TituloReceitaDespesa obj) {
        TituloReceitaDespesaDataContract objDataContract = new TituloReceitaDespesaDataContract();
        BeanUtils.copyProperties(obj, objDataContract);
        objDataContract.setUsuario(new UsuarioDataContract(null, obj.getUsuario().getNome(),
                obj.getUsuario().getEmail()));
        return objDataContract;
    }
}