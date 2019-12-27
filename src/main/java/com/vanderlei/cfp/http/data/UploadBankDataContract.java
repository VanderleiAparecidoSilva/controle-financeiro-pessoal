package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadBankDataContract {

  private static final long serialVersionUID = 1L;

  private String id;

  @Valid @NotNull private String descricao;

  @Valid @NotNull private String centroCustoPrimario;

  @Valid @NotNull private String centroCustoSecundario;

  private Integer diaVencimento;

  private String contaBancaria;

  private Integer linhaInicialArquivo;

  private String tipoArquivo;

  private String tipo;

  @Valid
  @NotNull
  @JsonProperty("usuario")
  private UsuarioDataContract usuario;

  public UploadBankDataContract() {
    this.diaVencimento = 10;
    this.contaBancaria = "SANTANDER";
  }

  public UploadBankDataContract(
      final String id,
      final String descricao,
      final String centroCustoPrimario,
      final String centroCustoSecundario,
      final Integer diaVencimento,
      final String contaBancaria,
      final Integer linhaInicialArquivo,
      final String tipoArquivo,
      final String tipo,
      final UsuarioDataContract usuario) {
    this.id = id;
    this.descricao = descricao;
    this.centroCustoPrimario = centroCustoPrimario;
    this.centroCustoSecundario = centroCustoSecundario;
    this.diaVencimento = diaVencimento;
    this.contaBancaria = contaBancaria;
    this.linhaInicialArquivo = linhaInicialArquivo;
    this.tipoArquivo = tipoArquivo;
    this.tipo = tipo;
    this.usuario = usuario;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Integer getDiaVencimento() {
    return diaVencimento;
  }

  public void setDiaVencimento(Integer diaVencimento) {
    this.diaVencimento = diaVencimento;
  }

  public String getContaBancaria() {
    return contaBancaria;
  }

  public void setContaBancaria(String contaBancaria) {
    this.contaBancaria = contaBancaria;
  }

  public Integer getLinhaInicialArquivo() {
    return linhaInicialArquivo;
  }

  public void setLinhaInicialArquivo(Integer linhaInicialArquivo) {
    this.linhaInicialArquivo = linhaInicialArquivo;
  }

  public String getTipoArquivo() {
    return tipoArquivo;
  }

  public void setTipoArquivo(String tipoArquivo) {
    this.tipoArquivo = tipoArquivo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public UsuarioDataContract getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDataContract usuario) {
    this.usuario = usuario;
  }
}
