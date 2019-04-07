package com.vanderlei.cfp.entities;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "contaBancaria")
@CompoundIndexes({
  @CompoundIndex(name = "nome", def = "{'nome' : 1}"),
  @CompoundIndex(name = "numerocontabancaria", def = "{'numeroContaBancaria' : 1}"),
  @CompoundIndex(name = "nome-usuarionome", def = "{'nome' : 1, 'usuario.nome' : 1}"),
  @CompoundIndex(name = "nome-usuarioemail", def = "{'nome' : 1, 'usuario.email' : 1}")
})
public class ContaBancaria implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private String id;

  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String nome;

  private String numeroContaBancaria;

  private BigDecimal limiteContaBancaria;

  private BigDecimal saldoContaBancaria;

  private Boolean vincularSaldoBancarioNoTotalReceita;

  private Boolean atualizarSaldoBancarioNaBaixaTitulo;

  private Boolean contaBancariaPadrao;

  @NotNull private Usuario usuario;

  @NotEmpty private LocalDateTime dataInclusao;

  private LocalDateTime dataAlteracao;

  private LocalDateTime dataExclusao;

  public ContaBancaria() {}

  public ContaBancaria(
      final String id,
      final String nome,
      final String numeroContaBancaria,
      final BigDecimal limiteContaBancaria,
      final BigDecimal saldoContaBancaria,
      final Boolean vincularSaldoBancarioNoTotalReceita,
      final Boolean atualizarSaldoBancarioNaBaixaTitulo,
      final Usuario usuario) {
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

  public Boolean getContaBancariaPadrao() {
    return contaBancariaPadrao;
  }

  public void setContaBancariaPadrao(Boolean contaBancariaPadrao) {
    this.contaBancariaPadrao = contaBancariaPadrao;
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
