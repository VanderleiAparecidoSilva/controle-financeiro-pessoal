package com.vanderlei.cfp.entities;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "contabancaria")
public class ContaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
    private String nome;

    private String numeroContaBancaria;

    private Double limiteContaBancaria;

    private Double saldoContaBancaria;

    private Boolean vincularSaldoBancarioNoSaldoFinal;

    private Boolean atualizarSaldoBancarioNaBaixaTitulo;

    @NotNull(message = "O usuario deve ser informado")
    private Usuario usuario;

    @NotEmpty(message = "Preenchimento obrigatório")
    private LocalDateTime dataInclusao;

    private LocalDateTime dataAlteracao;

    private LocalDateTime dataExclusao;

    public ContaBancaria() {
    }

    public ContaBancaria(final String id, final String nome, final String numeroContaBancaria,
                         final Double limiteContaBancaria, final Double saldoContaBancaria,
                         final Boolean vincularSaldoBancarioNoSaldoFinal,
                         final Boolean atualizarSaldoBancarioNaBaixaTitulo, final Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.numeroContaBancaria = numeroContaBancaria;
        this.limiteContaBancaria = limiteContaBancaria;
        this.saldoContaBancaria = saldoContaBancaria;
        this.vincularSaldoBancarioNoSaldoFinal = vincularSaldoBancarioNoSaldoFinal;
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

    public Boolean getVincularSaldoBancarioNoSaldoFinal() {
        return vincularSaldoBancarioNoSaldoFinal;
    }

    public void setVincularSaldoBancarioNoSaldoFinal(Boolean vincularSaldoBancarioNoSaldoFinal) {
        this.vincularSaldoBancarioNoSaldoFinal = vincularSaldoBancarioNoSaldoFinal;
    }

    public Boolean getAtualizarSaldoBancarioNaBaixaTitulo() {
        return atualizarSaldoBancarioNaBaixaTitulo;
    }

    public void setAtualizarSaldoBancarioNaBaixaTitulo(Boolean atualizarSaldoBancarioNaBaixaTitulo) {
        this.atualizarSaldoBancarioNaBaixaTitulo = atualizarSaldoBancarioNaBaixaTitulo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public LocalDateTime getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(LocalDateTime dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public boolean getAtivo() {
        return this.dataExclusao == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContaBancaria that = (ContaBancaria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}