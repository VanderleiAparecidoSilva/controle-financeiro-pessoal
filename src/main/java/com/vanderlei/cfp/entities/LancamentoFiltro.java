package com.vanderlei.cfp.entities;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

@Builder
public class LancamentoFiltro implements Serializable {

  private String nome;

  public LancamentoFiltro(final String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(final String nome) {
    this.nome = nome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LancamentoFiltro that = (LancamentoFiltro) o;
    return Objects.equals(nome, that.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome);
  }
}
