package com.vanderlei.cfp.entities.upload;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Document(collection = "upload")
@CompoundIndexes({@CompoundIndex(name = "tipo", def = "{'tipo' : 1}")})
public class CentroCustoUpload extends Upload {

  @NotEmpty
  @Length(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
  private String nome;

  private Boolean primaria;

  private Boolean secundaria;

  private Boolean aplicarNaReceita;

  private Boolean aplicarNaDespesa;

  public CentroCustoUpload() {}

  public CentroCustoUpload(
      final String nome,
      final Boolean primaria,
      final Boolean secundaria,
      final Boolean aplicarNaReceita,
      final Boolean aplicarNaDespesa) {
    this.nome = nome;
    this.primaria = primaria;
    this.secundaria = secundaria;
    this.aplicarNaReceita = aplicarNaReceita;
    this.aplicarNaDespesa = aplicarNaDespesa;
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

  public Boolean getAplicarNaReceita() {
    return aplicarNaReceita;
  }

  public void setAplicarNaReceita(Boolean aplicarNaReceita) {
    this.aplicarNaReceita = aplicarNaReceita;
  }

  public Boolean getAplicarNaDespesa() {
    return aplicarNaDespesa;
  }

  public void setAplicarNaDespesa(Boolean aplicarNaDespesa) {
    this.aplicarNaDespesa = aplicarNaDespesa;
  }
}
