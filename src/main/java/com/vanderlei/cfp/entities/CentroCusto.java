package com.vanderlei.cfp.entities;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "centrocusto")
@CompoundIndexes(
        {
                @CompoundIndex(name = "nome", def = "{'nome' : 1}"),
                @CompoundIndex(name = "nome-usuarionome", def = "{'nome' : 1, 'usuario.nome' : 1}"),
                @CompoundIndex(name = "nome-usuarioemail", def = "{'nome' : 1, 'usuario.email' : 1}")
        }
)
public class CentroCusto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
    private String nome;

    private Boolean aplicarNaReceita;

    private Boolean aplicarNaDespesa;

    @NotNull(message = "O usuario deve ser informado")
    private Usuario usuario;

    @NotEmpty(message = "Preenchimento obrigatório")
    private LocalDateTime dataInclusao;

    private LocalDateTime dataAlteracao;

    private LocalDateTime dataExclusao;

    public CentroCusto() {
    }

    public CentroCusto(final String id, final String nome, final Boolean aplicarNaReceita, final Boolean aplicarNaDespesa, final Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.aplicarNaReceita = aplicarNaReceita;
        this.aplicarNaDespesa = aplicarNaDespesa;
        this.usuario = usuario;
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

    public boolean getAtivo() {
        return this.dataExclusao == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CentroCusto that = (CentroCusto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}