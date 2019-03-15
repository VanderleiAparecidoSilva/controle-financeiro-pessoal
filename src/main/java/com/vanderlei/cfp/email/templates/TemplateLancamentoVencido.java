package com.vanderlei.cfp.email.templates;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class TemplateLancamentoVencido {

    private Usuario usuario;

    private List<Lancamento> lancamentos;

    public TemplateLancamentoVencido() {
    }

    public TemplateLancamentoVencido(Usuario usuario, List<Lancamento> lancamentos) {
        this.usuario = usuario;
        this.lancamentos = lancamentos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Lancamento> getLancamentos() {
        return lancamentos;
    }

    public void setLancamentos(List<Lancamento> lancamentos) {
        this.lancamentos = lancamentos;
    }

    public BigDecimal getValorTotal() {
        BigDecimal soma = BigDecimal.valueOf(0.0);
        for (Lancamento item : this.lancamentos) {
            soma = soma.add(item.getValorParcela());
        }
        return soma;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Lançamentos{");
        sb.append(" Usuário: ");
        sb.append(getUsuario().getNome());
        sb.append("\nLançamentos:\n");
        for (Lancamento item : getLancamentos()) {
            sb.append(item.toString());
        }
        return sb.toString();
    }
}