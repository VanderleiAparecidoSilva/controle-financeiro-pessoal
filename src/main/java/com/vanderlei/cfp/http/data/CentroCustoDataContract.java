package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class CentroCustoDataContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String id;

    @Valid
    @NotNull
    @JsonProperty("usuario")
    private UsuarioDataContract usuario;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
    private String nome;

    private Boolean aplicarNaDespesa;

    private Boolean aplicarNaReceita;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UsuarioDataContract getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDataContract usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAplicarNaDespesa() {
        return aplicarNaDespesa;
    }

    public void setAplicarNaDespesa(Boolean aplicarNaDespesa) {
        this.aplicarNaDespesa = aplicarNaDespesa;
    }

    public Boolean getAplicarNaReceita() {
        return aplicarNaReceita;
    }

    public void setAplicarNaReceita(Boolean aplicarNaReceita) {
        this.aplicarNaReceita = aplicarNaReceita;
    }

    public CentroCustoDataContract() {
    }

    public CentroCustoDataContract(final String id, final UsuarioDataContract usuario, final String nome,
                                   final Boolean aplicarNaDespesa, final Boolean aplicarNaReceita) {
        this.id = id;
        this.usuario = usuario;
        this.nome = nome;
        this.aplicarNaDespesa = aplicarNaDespesa;
        this.aplicarNaReceita = aplicarNaReceita;
    }
}