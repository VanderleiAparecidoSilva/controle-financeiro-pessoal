package com.vanderlei.cfp.entities;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Baixa implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    private String id;

    private LocalDateTime data;

    private String observacao;

    @NotNull(message = "O usuario deve ser informado")
    private Usuario usuario;

    public Baixa() {
    }

    public Baixa(String id, LocalDateTime data, String observacao, @NotNull(message = "O usuario deve ser informado") Usuario usuario) {
        this.id = id;
        this.data = data;
        this.observacao = observacao;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
