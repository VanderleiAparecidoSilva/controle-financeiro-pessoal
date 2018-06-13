package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class BaixaDataContract implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime data;

    private String observacao;

    @Valid
    @JsonProperty("contaBancaria")
    private ContaBancariaDataContract contaBancaria;

    @Valid
    @NotNull
    @JsonProperty("usuario")
    private UsuarioDataContract usuario;

    public BaixaDataContract() {
    }

    public BaixaDataContract(final LocalDateTime data, final String observacao,
                             final ContaBancariaDataContract contaBancaria, final UsuarioDataContract usuario) {
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

    public ContaBancariaDataContract getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancariaDataContract contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public UsuarioDataContract getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDataContract usuario) {
        this.usuario = usuario;
    }
}