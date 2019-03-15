package com.vanderlei.cfp.http.data.params;

import com.vanderlei.cfp.entities.enums.Operacao;

import java.math.BigDecimal;

public class ContaBancariaSaldoDataContract {

  private BigDecimal valor;

  private Operacao operacao;

  public ContaBancariaSaldoDataContract() {}

  public ContaBancariaSaldoDataContract(BigDecimal valor, Operacao operacao) {
    this.valor = valor;
    this.operacao = operacao;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Operacao getOperacao() {
    return operacao;
  }

  public void setOperacao(Operacao operacao) {
    this.operacao = operacao;
  }
}
