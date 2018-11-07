package com.vanderlei.cfp.http.data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PermissaoUsuarioDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @NotNull @NotEmpty private String idUsuario;

  @NotNull @NotEmpty private String idPermissao;

  private Boolean ativo;

  public PermissaoUsuarioDataContract() {}

  public PermissaoUsuarioDataContract(
      final String id, final String idUsuario, final String idPermissao) {
    this.id = id;
    this.idUsuario = idUsuario;
    this.idPermissao = idPermissao;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(String idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getIdPermissao() {
    return idPermissao;
  }

  public void setIdPermissao(String idPermissao) {
    this.idPermissao = idPermissao;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }
}
