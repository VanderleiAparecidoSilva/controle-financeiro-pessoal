package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ContaBancariaDataContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
    private String nome;

    private String numeroContaBancaria;

    private Double limiteContaBancaria;

    private Double saldoContaBancaria;

    private Boolean vincularSaldoBancarioNoTotalReceita;

    private Boolean atualizarSaldoBancarioNaBaixaTitulo;

    @Valid
    @NotNull
    @JsonProperty("usuario")
    private UsuarioDataContract usuario;

    public ContaBancariaDataContract() {
    }

    public ContaBancariaDataContract(final String id, final String nome, final String numeroContaBancaria,
                                     final Double limiteContaBancaria, final Double saldoContaBancaria,
                                     final Boolean vincularSaldoBancarioNoTotalReceita,
                                     final Boolean atualizarSaldoBancarioNaBaixaTitulo, final UsuarioDataContract usuario) {
        this.id = id;
        this.nome = nome;
        this.numeroContaBancaria = numeroContaBancaria;
        this.limiteContaBancaria = limiteContaBancaria;
        this.saldoContaBancaria = saldoContaBancaria;
        this.vincularSaldoBancarioNoTotalReceita = vincularSaldoBancarioNoTotalReceita;
        this.atualizarSaldoBancarioNaBaixaTitulo = atualizarSaldoBancarioNaBaixaTitulo;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroContaBancaria() {
        return numeroContaBancaria;
    }

    public void setNumeroContaBancaria(String numeroContaBancaria) {
        this.numeroContaBancaria = numeroContaBancaria;
    }

    public Double getLimiteContaBancaria() {
        return limiteContaBancaria;
    }

    public void setLimiteContaBancaria(Double limiteContaBancaria) {
        this.limiteContaBancaria = limiteContaBancaria;
    }

    public Double getSaldoContaBancaria() {
        return saldoContaBancaria;
    }

    public void setSaldoContaBancaria(Double saldoContaBancaria) {
        this.saldoContaBancaria = saldoContaBancaria;
    }

    public Boolean getVincularSaldoBancarioNoTotalReceita() {
        return vincularSaldoBancarioNoTotalReceita;
    }

    public void setVincularSaldoBancarioNoTotalReceita(Boolean vincularSaldoBancarioNoTotalReceita) {
        this.vincularSaldoBancarioNoTotalReceita = vincularSaldoBancarioNoTotalReceita;
    }

    public Boolean getAtualizarSaldoBancarioNaBaixaTitulo() {
        return atualizarSaldoBancarioNaBaixaTitulo;
    }

    public void setAtualizarSaldoBancarioNaBaixaTitulo(Boolean atualizarSaldoBancarioNaBaixaTitulo) {
        this.atualizarSaldoBancarioNaBaixaTitulo = atualizarSaldoBancarioNaBaixaTitulo;
    }

    public UsuarioDataContract getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDataContract usuario) {
        this.usuario = usuario;
    }
}