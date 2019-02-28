package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CentroCustoDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String nome;

  private Boolean primaria;

  private Boolean secundaria;

  private Boolean aplicarNaDespesa;

  private Boolean aplicarNaReceita;

  private Boolean ativo;

  @JsonIgnore private LocalDateTime dataInclusao;

  @JsonIgnore private LocalDateTime dataAlteracao;

  @JsonIgnore private LocalDateTime dataExclusao;

  @Valid
  @NotNull
  @JsonProperty("usuario")
  private UsuarioDataContract usuario;

  public CentroCustoDataContract() {}

  public CentroCustoDataContract(
      final String id,
      final String nome,
      final Boolean primaria,
      final Boolean secundaria,
      final Boolean aplicarNaDespesa,
      final Boolean aplicarNaReceita,
      final UsuarioDataContract usuario) {
    this.id = id;
    this.nome = nome;
    this.primaria = primaria;
    this.secundaria = secundaria;
    this.aplicarNaDespesa = aplicarNaDespesa;
    this.aplicarNaReceita = aplicarNaReceita;
    this.usuario = usuario;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UsuarioDataContract getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDataContract usuario) {
    this.usuario = usuario;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Boolean getPrimaria() {
    return primaria;
  }

  public void setPrimaria(Boolean primaria) {
    this.primaria = primaria;
  }

  public Boolean getSecundaria() {
    return secundaria;
  }

  public void setSecundaria(Boolean secundaria) {
    this.secundaria = secundaria;
  }

  public Boolean getAplicarNaDespesa() {
    return aplicarNaDespesa;
  }

  public void setAplicarNaDespesa(Boolean aplicarNaDespesa) {
    this.aplicarNaDespesa = aplicarNaDespesa;
  }

  public Boolean getAplicarNaReceita() {
    return aplicarNaReceita;
  }

  public void setAplicarNaReceita(Boolean aplicarNaReceita) {
    this.aplicarNaReceita = aplicarNaReceita;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
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
