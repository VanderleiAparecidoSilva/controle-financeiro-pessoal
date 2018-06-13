package com.vanderlei.cfp.entities.enums;

public enum Operacao {

    CREDITO(1, "Crédito"),
    DEBITO(2, "Débito");

    private Integer codigo;
    private String descricao;

    Operacao(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Operacao toEnum(final Integer codigo) {
        if (codigo == null) {
            return null;
        }

        for (Operacao x : Operacao.values()) {
            if (codigo.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + codigo);
    }
}