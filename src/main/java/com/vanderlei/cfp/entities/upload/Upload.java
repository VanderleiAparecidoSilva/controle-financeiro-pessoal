package com.vanderlei.cfp.entities.upload;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.entities.enums.TipoUpload;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "upload")
@CompoundIndexes({@CompoundIndex(name = "tipo", def = "{'tipo' : 1}")})
public class Upload implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private String id;

  private TipoUpload tipo;

  @NotNull private Usuario usuario;

  @NotNull private LocalDateTime dataInclusao;

  @NotNull private Boolean processado;

  public Upload() {}

  public Upload(final String id, final TipoUpload tipo, final Usuario usuario) {
    this.id = id;
    this.tipo = tipo;
    this.usuario = usuario;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TipoUpload getTipo() {
    return tipo;
  }

  public void setTipo(TipoUpload tipo) {
    this.tipo = tipo;
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

  public Boolean getProcessado() {
    return processado;
  }

  public void setProcessado(Boolean processado) {
    this.processado = processado;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Upload that = (Upload) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
