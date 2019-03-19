package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.http.data.*;
import org.springframework.stereotype.Component;

@Component
public class Parsers {

  public static void parse(
      final String id, final CentroCusto obj, final CentroCustoDataContract dataContract) {
    obj.setId(id);
    obj.setNome(dataContract.getNome().toUpperCase());
    obj.setPrimaria(dataContract.getPrimaria());
    obj.setSecundaria(dataContract.getSecundaria());
    obj.setAplicarNaDespesa(dataContract.getAplicarNaDespesa());
    obj.setAplicarNaReceita(dataContract.getAplicarNaReceita());
  }

  public static void parse(
      final String id, final Usuario obj, final UsuarioDataContract dataContract) {
    obj.setId(id);
    obj.setNome(dataContract.getNome());
    obj.setEmail(dataContract.getEmail());
    obj.setEmailCC(dataContract.getEmailCC());
  }

  public static void parse(
      final String id, final ContaBancaria obj, final ContaBancariaDataContract dataContract) {
    obj.setId(id);
    obj.setNome(dataContract.getNome().toUpperCase());
    obj.setNumeroContaBancaria(dataContract.getNumeroContaBancaria());
    obj.setAtualizarSaldoBancarioNaBaixaTitulo(
        dataContract.getAtualizarSaldoBancarioNaBaixaTitulo());
    obj.setVincularSaldoBancarioNoTotalReceita(
        dataContract.getVincularSaldoBancarioNoTotalReceita());
    obj.setLimiteContaBancaria(dataContract.getLimiteContaBancaria());
    obj.setSaldoContaBancaria(dataContract.getSaldoContaBancaria());
  }

  public static void parse(
      final String id,
      final TituloLancamento obj,
      final TituloLancamentoDataContract dataContract) {
    obj.setId(id);
    obj.setNome(dataContract.getNome());
    obj.setDiaVencimento(dataContract.getDiaVencimento());
    obj.setAplicarNaDespesa(dataContract.getAplicarNaDespesa());
    obj.setAplicarNaReceita(dataContract.getAplicarNaReceita());
  }

  public static void parse(
      final String id, final Permissao obj, final PermissaoDataContract dataContract) {
    obj.setId(id);
    obj.setDescricao(dataContract.getDescricao());
  }
}
