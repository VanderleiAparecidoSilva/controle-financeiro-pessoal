package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @NotEmpty
  @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
  private String nome;

  @NotEmpty @Email private String email;

  private String emailCC;

  private String senha;

  private Boolean permiteEmailLembrete;

  public UsuarioDataContract() {}

  public UsuarioDataContract(
      final String id,
      final String nome,
      final String email,
      final String emailCC,
      final String senha,
      final Boolean permiteEmailLembrete) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.emailCC = emailCC;
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

  public String getEmailCC() {
    return emailCC;
  }

  public void setEmailCC(String emailCC) {
    this.emailCC = emailCC;
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
