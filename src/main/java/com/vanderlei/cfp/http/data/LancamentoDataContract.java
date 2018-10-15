package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class LancamentoDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @JsonIgnore private UUID uuid;

  @NotNull private TituloLancamentoDataContract nome;

  @Valid
  @NotNull
  @JsonProperty("centrocusto")
  private CentroCustoDataContract centroCusto;

  @NotNull private LocalDate vencimento;

  @NotNull private Double valorParcela;

  @NotNull private int parcela;

  @NotNull private int quantidadeTotalParcelas;

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
      final UUID uuid,
      final TituloLancamentoDataContract nome,
      final CentroCustoDataContract centroCusto,
      final LocalDate vencimento,
      final Double valorParcela,
      final int parcela,
      final int quantidadeParcelas,
      final boolean gerarParcelaUnica,
      final ContaBancariaDataContract contaBancaria,
      final String observacao,
      final Status status,
      final Tipo tipo,
      final UsuarioDataContract usuario,
      final BaixaDataContract baixa) {
    this.id = id;
    this.uuid = uuid;
    this.nome = nome;
    this.centroCusto = centroCusto;
    this.vencimento = vencimento;
    this.valorParcela = valorParcela;
    this.parcela = parcela;
    this.quantidadeTotalParcelas = quantidadeParcelas;
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

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
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

  public int getParcela() {
    return parcela;
  }

  public void setParcela(int parcela) {
    this.parcela = parcela;
  }

  public int getQuantidadeTotalParcelas() {
    return quantidadeTotalParcelas;
  }

  public void setQuantidadeTotalParcelas(int quantidadeTotalParcelas) {
    this.quantidadeTotalParcelas = quantidadeTotalParcelas;
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
