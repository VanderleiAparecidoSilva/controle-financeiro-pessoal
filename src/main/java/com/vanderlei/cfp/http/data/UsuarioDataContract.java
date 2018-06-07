package com.vanderlei.cfp.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class UsuarioDataContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private String id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max = 100, message = "O nome deve conter entre 5 e 100 caracteres")
    private String nome;

    @Email
    private String email;

    public UsuarioDataContract() {
    }

    public UsuarioDataContract(final String id, final String nome, final String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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
}
