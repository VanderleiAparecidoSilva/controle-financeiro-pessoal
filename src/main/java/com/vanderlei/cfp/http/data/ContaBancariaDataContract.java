package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaBancariaDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @NotEmpty
  @Length(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
  private String nome;

  private String numeroContaBancaria;

  private BigDecimal limiteContaBancaria;

  private BigDecimal saldoContaBancaria;

  private Boolean vincularSaldoBancarioNoTotalReceita;

  private Boolean atualizarSaldoBancarioNaBaixaTitulo;

  private Boolean ativo;

  @JsonIgnore private LocalDateTime dataInclusao;

  @JsonIgnore private LocalDateTime dataAlteracao;

  @JsonIgnore private LocalDateTime dataExclusao;

  @Valid
  @NotNull
  @JsonProperty("usuario")
  private UsuarioDataContract usuario;

  public ContaBancariaDataContract() {}

  public ContaBancariaDataContract(
      final String id,
      final String nome,
      final String numeroContaBancaria,
      final BigDecimal limiteContaBancaria,
      final BigDecimal saldoContaBancaria,
      final Boolean vincularSaldoBancarioNoTotalReceita,
      final Boolean atualizarSaldoBancarioNaBaixaTitulo,
      final UsuarioDataContract usuario) {
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

  public BigDecimal getLimiteContaBancaria() {
    return limiteContaBancaria;
  }

  public void setLimiteContaBancaria(BigDecimal limiteContaBancaria) {
    this.limiteContaBancaria = limiteContaBancaria;
  }

  public BigDecimal getSaldoContaBancaria() {
    return saldoContaBancaria;
  }

  public void setSaldoContaBancaria(BigDecimal saldoContaBancaria) {
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

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public UsuarioDataContract getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDataContract usuario) {
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
}
