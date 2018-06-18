package com.vanderlei.cfp.http.data;

import java.io.Serializable;

public class CredenciaisDataContract implements Serializable {

    private String email;

    private String senha;

    public CredenciaisDataContract() {
    }

    public CredenciaisDataContract(String email, String senha) {
        this.email = email;
        this.senha = senha;
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
}
