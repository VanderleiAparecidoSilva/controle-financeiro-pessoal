package com.vanderlei.cfp.entities.enums;

public enum Status {

    ABERTO(1, "Em aberto"),
    RECEBIDO(2, "Recebido"),
    PAGO(3, "Pago");

    private Integer codigo;
    private String descricao;

    Status(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Status toEnum(final Integer codigo) {
        if (codigo == null) {
            return null;
        }

        for (Status x : Status.values()) {
            if (codigo.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + codigo);
    }
}
