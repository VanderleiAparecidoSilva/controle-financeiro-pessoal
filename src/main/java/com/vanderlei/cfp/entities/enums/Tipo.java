package com.vanderlei.cfp.entities.enums;

public enum Tipo {
  DESPESA(1, "Despesa"),
  RECEITA(2, "Receita");

  private Integer codigo;
  private String descricao;

  Tipo(Integer codigo, String descricao) {
    this.codigo = codigo;
    this.descricao = descricao;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public static Tipo toEnum(final Integer codigo) {
    if (codigo == null) {
      return null;
    }

    for (Tipo x : Tipo.values()) {
      if (codigo.equals(x.getCodigo())) {
        return x;
      }
    }

    throw new IllegalArgumentException("Id inválido: " + codigo);
  }
}
