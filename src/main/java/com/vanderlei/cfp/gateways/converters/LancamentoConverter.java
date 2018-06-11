package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LancamentoConverter implements Converter<LancamentoDataContract, Lancamento> {

    @Override
    public Lancamento convert(final LancamentoDataContract dataContract) {
        Lancamento obj = new Lancamento();
        BeanUtils.copyProperties(dataContract, obj);
        if (dataContract.getNome() != null) {
            obj.setNome(new TituloLancamento(null, dataContract.getNome().getNome(),
                    dataContract.getNome().getDiaVencimento(), dataContract.getNome().getAplicarNaReceita(),
                    dataContract.getNome().getAplicarNaDespesa(), new Usuario(null, dataContract.getNome().getNome(),
                    dataContract.getNome().getUsuario().getEmail())));
        }
        if (dataContract.getCentroCusto() != null) {
            obj.setCentroCusto(new CentroCusto(null, dataContract.getCentroCusto().getNome(),
                    dataContract.getCentroCusto().getAplicarNaReceita(),
                    dataContract.getCentroCusto().getAplicarNaDespesa(),
                    new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getCentroCusto().getUsuario().getEmail())));
        }
        if (dataContract.getContaBancaria() != null) {
            obj.setContaBancaria(new ContaBancaria(null, dataContract.getContaBancaria().getNome(),
                    dataContract.getContaBancaria().getNumeroContaBancaria(),
                    dataContract.getContaBancaria().getLimiteContaBancaria(),
                    dataContract.getContaBancaria().getSaldoContaBancaria(),
                    dataContract.getContaBancaria().getVincularSaldoBancarioNoSaldoFinal(),
                    dataContract.getContaBancaria().getAtualizarSaldoBancarioNaBaixaTitulo(),
                    new Usuario(null, dataContract.getContaBancaria().getUsuario().getNome(),
                            dataContract.getContaBancaria().getUsuario().getEmail())));
        }
        obj.setUsuario(new Usuario(null, dataContract.getUsuario().getNome(), dataContract.getUsuario().getEmail()));

        return obj;
    }
}