package com.vanderlei.cfp.entities.enums;

public enum Perfil {

    ADMIN(1, "ROLE_ADMIN"),
    USUARIO(2, "ROLE_USUARIO");

    private Integer codigo;
    private String descricao;

    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Perfil toEnum(final Integer codigo) {
        if (codigo == null) {
            return null;
        }

        for (Perfil x : Perfil.values()) {
            if (codigo.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + codigo);
    }
}