package com.vanderlei.cfp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vanderlei.cfp.entities.enums.Perfil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

  @JsonIgnore private String senha;

  @NotNull private Boolean permiteEmailLembrete;

  @NotNull private LocalDateTime dataInclusao;

  private LocalDateTime dataAlteracao;

  private LocalDateTime dataExclusao;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "PERFIS")
  private Set<Integer> perfis;

  public Usuario() {
    this.perfis = new HashSet<>();
    this.permiteEmailLembrete = true;
    addPerfil(Perfil.USUARIO);
  }

  public Usuario(
      final String id, final String nome, final String email, final Boolean permiteEmailLembrete) {
    this.perfis = new HashSet<>();
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.permiteEmailLembrete = permiteEmailLembrete;
    addPerfil(Perfil.USUARIO);
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

  public Set<Perfil> getPerfis() {
    return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
  }

  public void addPerfil(Perfil perfil) {
    perfis.add(perfil.getCodigo());
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