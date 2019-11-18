package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vanderlei.cfp.entities.upload.Upload;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LancamentoUploadDataContract implements Serializable {

  @Valid
  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String descricao;

  @Valid
  @NotNull
  private String centroCustoPrimario;

  @Valid
  @NotNull
  private String centroCustoSecundario;

  @Valid
  @NotNull
  private LocalDate vencimento;

  @Valid
  @NotNull
  private BigDecimal valorParcela;

  @Valid
  @NotNull
  private Integer parcela;

  @Valid
  @NotNull
  private Boolean gerarParcelaUnica;

  private String contaBancaria;

  private String observacao;

  @Valid
  @NotNull
  private String status;

  @Valid
  @NotNull
  private String tipoLancamento;

  public LancamentoUploadDataContract() {}

  public LancamentoUploadDataContract(
      final String descricao,
      final String centroCustoPrimario,
      final String centroCustoSecundario,
      final LocalDate vencimento,
      final BigDecimal valorParcela,
      final Integer parcela,
      final Boolean gerarParcelaUnica,
      final String contaBancaria,
      final String observacao,
      final String status,
      final String tipoLancamento) {
    this.descricao = descricao;
    this.centroCustoPrimario = centroCustoPrimario;
    this.centroCustoSecundario = centroCustoSecundario;
    this.vencimento = vencimento;
    this.valorParcela = valorParcela;
    this.parcela = parcela;
    this.gerarParcelaUnica = gerarParcelaUnica;
    this.contaBancaria = contaBancaria;
    this.observacao = observacao;
    this.status = status;
    this.tipoLancamento = tipoLancamento;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getCentroCustoPrimario() {
    return centroCustoPrimario;
  }

  public void setCentroCustoPrimario(String centroCustoPrimario) {
    this.centroCustoPrimario = centroCustoPrimario;
  }

  public String getCentroCustoSecundario() {
    return centroCustoSecundario;
  }

  public void setCentroCustoSecundario(String centroCustoSecundario) {
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

  public Integer getParcela() {
    return parcela;
  }

  public void setParcela(Integer parcela) {
    this.parcela = parcela;
  }

  public Boolean getGerarParcelaUnica() {
    return gerarParcelaUnica;
  }

  public void setGerarParcelaUnica(Boolean gerarParcelaUnica) {
    this.gerarParcelaUnica = gerarParcelaUnica;
  }

  public String getContaBancaria() {
    return contaBancaria;
  }

  public void setContaBancaria(String contaBancaria) {
    this.contaBancaria = contaBancaria;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTipoLancamento() {
    return tipoLancamento;
  }

  public void setTipoLancamento(String tipoLancamento) {
    this.tipoLancamento = tipoLancamento;
  }
}
