package com.vanderlei.cfp.entities;

import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "lancamento")
@CompoundIndexes(
        {
                @CompoundIndex(name = "nome", def = "{'nome' : 1}"),
                @CompoundIndex(name = "tipo", def = "{'tipo' : 1}"),
                @CompoundIndex(name = "status", def = "{'status' : 1}"),
                @CompoundIndex(name = "nome-usuarionome", def = "{'nome' : 1, 'usuario.nome' : 1}"),
                @CompoundIndex(name = "nome-usuarioemail", def = "{'nome' : 1, 'usuario.email' : 1}")
        }
)
public class Lancamento implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "Preenchimento obrigatório")
    private TituloLancamento nome;


    @NotNull(message = "Preenchimento obrigatório")
    private CentroCusto centroCusto;

    @NotNull(message = "Preenchimento obrigatório")
    private LocalDate vencimento;

    @NotNull(message = "Preenchimento obrigatório")
    private Double valor;

    @NotNull(message = "Preenchimento obrigatório")
    private int quantidadeParcelas;

    private ContaBancaria contaBancaria;

    private String observacao;

    @NotNull(message = "Preenchimento obrigatório")
    private Status status;

    @NotNull(message = "Preenchimento obrigatório")
    private Tipo tipo;

    @NotNull(message = "O usuario deve ser informado")
    private Usuario usuario;

    @NotEmpty(message = "Preenchimento obrigatório")
    private LocalDateTime dataInclusao;

    private LocalDateTime dataAlteracao;

    private LocalDateTime dataExclusao;

    public Lancamento() {
        this.tipo = Tipo.RECEITA;
    }

    public Lancamento(final String id, final TituloLancamento nome, final CentroCusto centroCusto, final LocalDate vencimento,
                      final Double valor, final int quantidadeParcelas, final ContaBancaria contaBancaria,
                      final String observacao, final Status status, final Tipo tipo, final Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.centroCusto = centroCusto;
        this.vencimento = vencimento;
        this.valor = valor;
        this.quantidadeParcelas = quantidadeParcelas;
        this.contaBancaria = contaBancaria;
        this.observacao = observacao;
        this.status = status;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TituloLancamento getNome() {
        return nome;
    }

    public void setNome(TituloLancamento nome) {
        this.nome = nome;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
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
}
