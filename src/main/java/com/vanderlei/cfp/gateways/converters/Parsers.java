package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.http.data.*;
import org.springframework.stereotype.Component;

@Component
public class Parsers {

    public static void parse(final String id, final CentroCusto obj, final CentroCustoDataContract dataContract) {
        UsuarioConverter usuarioConverter = new UsuarioConverter();

        obj.setId(id);
        obj.setNome(dataContract.getNome());
        obj.setAplicarNaDespesa(dataContract.getAplicarNaDespesa());
        obj.setAplicarNaReceita(dataContract.getAplicarNaReceita());
        obj.setUsuario(usuarioConverter.convert(dataContract.getUsuario()));
    }

    public static void parse(final String id, final Usuario obj, final UsuarioDataContract dataContract) {
        obj.setId(id);
        obj.setNome(dataContract.getNome());
        obj.setEmail(dataContract.getEmail());
    }

    public static void parse(final String id, final ContaBancaria obj, final ContaBancariaDataContract dataContract) {
        UsuarioConverter usuarioConverter = new UsuarioConverter();

        obj.setId(id);
        obj.setNome(dataContract.getNome());
        obj.setNumeroContaBancaria(dataContract.getNumeroContaBancaria());
        obj.setAtualizarSaldoBancarioNaBaixaTitulo(dataContract.getAtualizarSaldoBancarioNaBaixaTitulo());
        obj.setVincularSaldoBancarioNoSaldoFinal(dataContract.getVincularSaldoBancarioNoSaldoFinal());
        obj.setLimiteContaBancaria(dataContract.getLimiteContaBancaria());
        obj.setSaldoContaBancaria(dataContract.getSaldoContaBancaria());
        obj.setUsuario(usuarioConverter.convert(dataContract.getUsuario()));
    }

    public static void parse(final String id, final TituloReceitaDespesa obj, final TituloReceitaDespesaDataContract dataContract) {
        UsuarioConverter usuarioConverter = new UsuarioConverter();

        obj.setId(id);
        obj.setNome(dataContract.getNome());
        obj.setDiaVencimento(dataContract.getDiaVencimento());
        obj.setAplicarNaDespesa(dataContract.getAplicarNaDespesa());
        obj.setAplicarNaReceita(dataContract.getAplicarNaReceita());
        obj.setUsuario(usuarioConverter.convert(dataContract.getUsuario()));
    }
}