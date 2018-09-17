package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class LancamentoDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonIgnore private String id;

  @NotNull private TituloLancamentoDataContract nome;

  @Valid
  @NotNull
  @JsonProperty("centrocusto")
  private CentroCustoDataContract centroCusto;

  @NotNull private LocalDate vencimento;

  @NotNull private Double valorParcela;

  @NotNull private int quantidadeParcelas;

  private boolean gerarParcelaUnica;

  @Valid
  @JsonProperty("contabancaria")
  private ContaBancariaDataContract contaBancaria;

  private String observacao;

  @NotNull private Status status;

  @NotNull private Tipo tipo;

  @JsonProperty("baixa")
  private BaixaDataContract baixa;

  @Valid
  @NotNull
  @JsonProperty("usuario")
  private UsuarioDataContract usuario;

  public LancamentoDataContract() {
    this.tipo = Tipo.RECEITA;
  }

  public LancamentoDataContract(
      final String id,
      final TituloLancamentoDataContract nome,
      final CentroCustoDataContract centroCusto,
      final LocalDate vencimento,
      final Double valorParcela,
      final int quantidadeParcelas,
      final boolean gerarParcelaUnica,
      final ContaBancariaDataContract contaBancaria,
      final String observacao,
      final Status status,
      final Tipo tipo,
      final UsuarioDataContract usuario,
      final BaixaDataContract baixa) {
    this.id = id;
    this.nome = nome;
    this.centroCusto = centroCusto;
    this.vencimento = vencimento;
    this.valorParcela = valorParcela;
    this.quantidadeParcelas = quantidadeParcelas;
    this.gerarParcelaUnica = gerarParcelaUnica;
    this.contaBancaria = contaBancaria;
    this.observacao = observacao;
    this.status = status;
    this.tipo = tipo;
    this.usuario = usuario;
    this.baixa = baixa;
    this.tipo = Tipo.RECEITA;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TituloLancamentoDataContract getNome() {
    return nome;
  }

  public void setNome(TituloLancamentoDataContract nome) {
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

  public Double getValorParcela() {
    return valorParcela;
  }

  public void setValorParcela(Double valorParcela) {
    this.valorParcela = valorParcela;
  }

  public int getQuantidadeParcelas() {
    return quantidadeParcelas;
  }

  public void setQuantidadeParcelas(int quantidadeParcelas) {
    this.quantidadeParcelas = quantidadeParcelas;
  }

  public boolean isGerarParcelaUnica() {
    return gerarParcelaUnica;
  }

  public void setGerarParcelaUnica(boolean gerarParcelaUnica) {
    this.gerarParcelaUnica = gerarParcelaUnica;
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

  public BaixaDataContract getBaixa() {
    return baixa;
  }

  public void setBaixa(BaixaDataContract baixa) {
    this.baixa = baixa;
  }
}
