package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LancamentoConverter implements Converter<LancamentoDataContract, Lancamento> {

    @Override
    public Lancamento convert(final LancamentoDataContract dataContract) {
        Lancamento obj = new Lancamento();
        BeanUtils.copyProperties(dataContract, obj);
        if (dataContract.getNome() != null) {
            obj.setNome(new TituloLancamento(null, dataContract.getNome().getNome(),
                    0, false, false,
                    new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getNome().getUsuario().getEmail())));
        }
        if (dataContract.getCentroCusto() != null) {
            obj.setCentroCusto(new CentroCusto(null, dataContract.getCentroCusto().getNome(),
                    false, false,
                    new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getCentroCusto().getUsuario().getEmail())));
        }
        if (dataContract.getContaBancaria() != null) {
            obj.setContaBancaria(new ContaBancaria(null, dataContract.getContaBancaria().getNome(),
                    null, null, null, null, null,
                    new Usuario(null, dataContract.getContaBancaria().getUsuario().getNome(),
                            dataContract.getContaBancaria().getUsuario().getEmail())));
        }
        if (dataContract.getBaixa() != null) {
            obj.setBaixa(new Baixa(null, LocalDateTime.now(), dataContract.getObservacao(),
                    new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail())));
        }
        obj.setUsuario(new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail()));

        return obj;
    }
}