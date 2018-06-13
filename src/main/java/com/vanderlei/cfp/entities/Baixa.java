package com.vanderlei.cfp.entities;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Baixa implements Serializable {

    public static final long serialVersionUID = 1L;

    private LocalDateTime data;

    private String observacao;

    private ContaBancaria contaBancaria;

    @NotNull(message = "O usuario deve ser informado")
    private Usuario usuario;

    public Baixa() {
    }

    public Baixa(final LocalDateTime data, final String observacao, final ContaBancaria contaBancaria,
                 final Usuario usuario) {
        this.data = data;
        this.observacao = observacao;
        this.contaBancaria = contaBancaria;
        this.usuario = usuario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
