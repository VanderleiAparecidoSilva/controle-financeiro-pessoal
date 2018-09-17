package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UsuarioDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonIgnore private String id;

  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String nome;

  @Email private String email;

  @NotEmpty private String senha;

  @NotNull private Boolean permiteEmailLembrete;

  public UsuarioDataContract() {}

  public UsuarioDataContract(
      final String id,
      final String nome,
      final String email,
      final String senha,
      final Boolean permiteEmailLembrete) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.permiteEmailLembrete = permiteEmailLembrete;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Boolean getPermiteEmailLembrete() {
    return permiteEmailLembrete;
  }

  public void setPermiteEmailLembrete(Boolean permiteEmailLembrete) {
    this.permiteEmailLembrete = permiteEmailLembrete;
  }
}
