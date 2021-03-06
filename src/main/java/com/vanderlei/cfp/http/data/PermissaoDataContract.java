package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissaoDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String descricao;

  @NotNull private Boolean permissaoDefault;

  private Boolean ativo;

  public PermissaoDataContract() {}

  public PermissaoDataContract(final String id, final String descricao, Boolean permissaoDefault) {
    this.id = id;
    this.descricao = descricao;
    this.permissaoDefault = permissaoDefault;
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

  public Boolean getPermissaoDefault() {
    return permissaoDefault;
  }

  public void setPermissaoDefault(Boolean permissaoDefault) {
    this.permissaoDefault = permissaoDefault;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }
}
