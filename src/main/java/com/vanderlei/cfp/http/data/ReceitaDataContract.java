package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class ReceitaDataContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String id;

    @NotNull(message = "Preenchimento obrigatório")
    private TituloReceitaDespesaDataContract nome;

    @Valid
    @NotNull(message = "Preenchimento obrigatório")
    @JsonProperty("centrocusto")
    private CentroCustoDataContract centroCusto;

    @NotNull(message = "Preenchimento obrigatório")
    private LocalDate vencimento;

    @NotNull(message = "Preenchimento obrigatório")
    private Double valor;

    @NotNull(message = "Preenchimento obrigatório")
    private int quantidadeParcelas;

    @Valid
    @JsonProperty("contabancaria")
    private ContaBancariaDataContract contaBancaria;

    private String observacao;

    @NotNull(message = "Preenchimento obrigatório")
    private Status status;

    @NotNull(message = "Preenchimento obrigatório")
    private Tipo tipo;

    @Valid
    @NotNull
    @JsonProperty("usuario")
    private UsuarioDataContract usuario;

    public ReceitaDataContract() {
        this.tipo = Tipo.RECEITA;
    }

    public ReceitaDataContract(final String id, final TituloReceitaDespesaDataContract nome,
                               final CentroCustoDataContract centroCusto, final LocalDate vencimento,
                               final Double valor, final int quantidadeParcelas,
                               final ContaBancariaDataContract contaBancaria, final String observacao, final Status status,
                               final Tipo tipo, final UsuarioDataContract usuario) {
        this.id = id;
        this.nome = nome;
        this.centroCusto = centroCusto;
        this.vencimento = vencimento;
        this.valor = valor;
        this.quantidadeParcelas = quantidadeParcelas;
        this.contaBancaria = contaBancaria;
        this.observacao = observacao;
        this.status = status;
        this.tipo = tipo;
        this.usuario = usuario;
        this.tipo = Tipo.RECEITA;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TituloReceitaDespesaDataContract getNome() {
        return nome;
    }

    public void setNome(TituloReceitaDespesaDataContract nome) {
        this.nome = nome;
    }

    public CentroCustoDataContract getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCustoDataContract centroCusto) {
        this.centroCusto = centroCusto;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public ContaBancariaDataContract getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancariaDataContract contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public UsuarioDataContract getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDataContract usuario) {
        this.usuario = usuario;
    }
}