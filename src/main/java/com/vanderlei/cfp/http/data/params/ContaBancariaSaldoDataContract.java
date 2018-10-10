package com.vanderlei.cfp.http.data.params;

import com.vanderlei.cfp.entities.enums.Operacao;

public class ContaBancariaSaldoDataContract {

  private Double valor;

  private Operacao operacao;

  public ContaBancariaSaldoDataContract() {

  }

  public ContaBancariaSaldoDataContract(Double valor, Operacao operacao) {
      this.valor = valor;
      this.operacao = operacao;
  }

  public Double getValor() {
      return valor;
  }

  public void setValor(Double valor) {
      this.valor = valor;
  }

  public Operacao getOperacao() {
      return operacao;
  }

  public void setOperacao(Operacao operacao) {
      this.operacao = operacao;
  }
}
