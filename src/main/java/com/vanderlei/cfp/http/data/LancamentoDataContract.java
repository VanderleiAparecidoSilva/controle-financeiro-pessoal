package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LancamentoDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @JsonIgnore private UUID uuid;

  @NotNull private String descricao;

  @Valid
  @NotNull
  @JsonProperty("centroCustoPrimario")
  private CentroCustoDataContract centroCustoPrimario;

  @Valid
  @NotNull
  @JsonProperty("centroCustoSecundario")
  private CentroCustoDataContract centroCustoSecundario;

  @NotNull private LocalDate vencimento;

  @NotNull private BigDecimal valorParcela;

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

  @Transient private Boolean parcelaAtrasada;

  public LancamentoDataContract() {
    this.tipo = Tipo.RECEITA;
  }

  public LancamentoDataContract(
      final String id,
      final UUID uuid,
      final String descricao,
      final CentroCustoDataContract centroCustoPrimario,
      final CentroCustoDataContract centroCustoSecundario,
      final LocalDate vencimento,
      final BigDecimal valorParcela,
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
    this.descricao = descricao;
    this.centroCustoPrimario = centroCustoPrimario;
    this.centroCustoSecundario = centroCustoSecundario;
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
    this.parcelaAtrasada = false;
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

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public CentroCustoDataContract getCentroCustoPrimario() {
    return centroCustoPrimario;
  }

  public void setCentroCustoPrimario(CentroCustoDataContract centroCustoPrimario) {
    this.centroCustoPrimario = centroCustoPrimario;
  }

  public CentroCustoDataContract getCentroCustoSecundario() {
    return centroCustoSecundario;
  }

  public void setCentroCustoSecundario(CentroCustoDataContract centroCustoSecundario) {
    this.centroCustoSecundario = centroCustoSecundario;
  }

  public LocalDate getVencimento() {
    return vencimento;
  }

  public void setVencimento(LocalDate vencimento) {
    this.vencimento = vencimento;
  }

  public BigDecimal getValorParcela() {
    return valorParcela;
  }

  public void setValorParcela(BigDecimal valorParcela) {
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

  public String getParcelaComTotalParcelas() {
    return StringUtils.leftPad(String.valueOf(this.parcela), 2, '0')
        + "/"
        + StringUtils.leftPad(String.valueOf(this.quantidadeTotalParcelas), 2, '0');
  }

  public Boolean getParcelaAtrasada() {
    return this.vencimento.isBefore(LocalDate.now());
  }

  public void setParcelaAtrasada(Boolean parcelaAtrasada) {
    this.parcelaAtrasada = parcelaAtrasada;
  }
}
