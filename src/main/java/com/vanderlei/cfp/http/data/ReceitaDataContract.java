package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.TituloReceitaDespesa;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class ReceitaDataContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String id;

    @NotNull(message = "Preenchimento obrigatório")
    private TituloReceitaDespesa nome;

    @Valid
    @NotNull(message = "Preenchimento obrigatório")
    @JsonProperty("centrocusto")
    private CentroCusto centroCusto;

    @NotNull(message = "Preenchimento obrigatório")
    private LocalDate vencimento;

    @NotNull(message = "Preenchimento obrigatório")
    private Double valor;

    @NotNull(message = "Preenchimento obrigatório")
    private int quantidadeParcelas;

    @Valid
    @JsonProperty("contabancaria")
    private ContaBancaria contaBancaria;

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

    public ReceitaDataContract(final String id, final TituloReceitaDespesa nome, final CentroCusto centroCusto,
                               final LocalDate vencimento, final Double valor, final int quantidadeParcelas,
                               final ContaBancaria contaBancaria, final String observacao, final Status status,
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

    public TituloReceitaDespesa getNome() {
        return nome;
    }

    public void setNome(TituloReceitaDespesa nome) {
        this.nome = nome;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
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

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
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