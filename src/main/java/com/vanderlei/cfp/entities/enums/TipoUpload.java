package com.vanderlei.cfp.entities.enums;

public enum TipoUpload {
  CENTRO_CUSTO("CC"),
  CONTA_BANCARIA("CB"),
  LANCAMENTO("LC");

  private String descricao;

  TipoUpload(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }

  public static TipoUpload toEnum(final String tipo) {
    if (tipo == null) {
      return null;
    }

    for (TipoUpload x : TipoUpload.values()) {
      if (tipo.equals(x.getDescricao())) {
        return x;
      }
    }

    throw new IllegalArgumentException("Tipo inválido: " + tipo);
  }
}
