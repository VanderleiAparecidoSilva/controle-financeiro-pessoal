package com.vanderlei.cfp.entities.upload;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Document(collection = "upload")
@CompoundIndexes({@CompoundIndex(name = "tipo", def = "{'tipo' : 1}")})
public class ContaBancariaUpload extends Upload {

  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String nome;

  private String numeroContaBancaria;

  private BigDecimal limiteContaBancaria;

  private BigDecimal saldoContaBancaria;

  private Boolean vincularSaldoBancarioNoTotalReceita;

  private Boolean atualizarSaldoBancarioNaBaixaTitulo;

  private Boolean contaBancariaPadrao;

  public ContaBancariaUpload() {}

  public ContaBancariaUpload(
      final String nome,
      final String numeroContaBancaria,
      final BigDecimal limiteContaBancaria,
      final BigDecimal saldoContaBancaria,
      final Boolean contaBancariaPadrao,
      final Boolean vincularSaldoBancarioNoTotalReceita,
      final Boolean atualizarSaldoBancarioNaBaixaTitulo) {
    this.nome = nome;
    this.numeroContaBancaria = numeroContaBancaria;
    this.limiteContaBancaria = limiteContaBancaria;
    this.saldoContaBancaria = saldoContaBancaria;
    this.contaBancariaPadrao = contaBancariaPadrao;
    this.vincularSaldoBancarioNoTotalReceita = vincularSaldoBancarioNoTotalReceita;
    this.atualizarSaldoBancarioNaBaixaTitulo = atualizarSaldoBancarioNaBaixaTitulo;
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
}
