package com.vanderlei.cfp.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "permissao_usuario")
@CompoundIndexes({
  @CompoundIndex(name = "idUsuario", def = "{'idUsuario' : 1}"),
  @CompoundIndex(name = "idPermissao", def = "{'idPermissao' : 1}"),
  @CompoundIndex(name = "idUsuario-idPermissao", def = "{'idUsuario' : 1, 'idPermissao' : 1}")
})
public class PermissaoUsuario implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private String id;

  @NotNull private String idUsuario;

  @NotNull private String idPermissao;

  @NotEmpty private LocalDateTime dataInclusao;

  private LocalDateTime dataAlteracao;

  private LocalDateTime dataExclusao;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PermissaoUsuario that = (PermissaoUsuario) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
