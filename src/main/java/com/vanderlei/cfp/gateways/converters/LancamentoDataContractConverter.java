package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LancamentoDataContractConverter implements Converter<Lancamento, LancamentoDataContract> {

    @Override
    public LancamentoDataContract convert(Lancamento obj) {
        LancamentoDataContract objDataContract = new LancamentoDataContract();
        BeanUtils.copyProperties(obj, objDataContract);
        objDataContract.setUsuario(new UsuarioDataContract(null, obj.getUsuario().getNome(),
                obj.getUsuario().getEmail(), null, obj.getUsuario().getPermiteEmailLembrete()));
        return objDataContract;
    }
}