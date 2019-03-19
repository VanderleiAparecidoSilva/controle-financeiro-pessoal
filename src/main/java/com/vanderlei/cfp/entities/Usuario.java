package com.vanderlei.cfp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "usuario")
@CompoundIndexes({
  @CompoundIndex(name = "nome", def = "{'nome' : 1}"),
  @CompoundIndex(name = "email", def = "{'email' : 1}"),
  @CompoundIndex(name = "nome-email", def = "{'nome' : 1, 'email' : 1}")
})
public class Usuario implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private String id;

  @NotNull @NotEmpty private String nome;

  @NotNull
  @NotEmpty
  @Column(unique = true)
  private String email;

  private String emailCC;

  @JsonIgnore private String senha;

  private Boolean permiteEmailLembrete;

  @NotNull private LocalDateTime dataInclusao;

  private LocalDateTime dataAlteracao;

  private LocalDateTime dataExclusao;

  public Usuario() {
    this.permiteEmailLembrete = true;
  }

  public Usuario(
      final String id,
      final String nome,
      final String email,
      final String emailCC,
      final Boolean permiteEmailLembrete) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.emailCC = emailCC;
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

  @JsonIgnore
  public boolean getAtivo() {
    return this.dataExclusao == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return Objects.equals(nome, usuario.nome) && Objects.equals(email, usuario.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, email);
  }

  @Override
  public String toString() {
    return "[Usuário] - " + "Nome = '" + nome + '\'' + ", Email = '" + email + '\'';
  }
}
