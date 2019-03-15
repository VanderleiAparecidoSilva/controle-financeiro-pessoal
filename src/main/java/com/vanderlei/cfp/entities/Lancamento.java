package com.vanderlei.cfp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Document(collection = "lancamento")
@CompoundIndexes({
  @CompoundIndex(name = "nome", def = "{'nome' : 1}"),
  @CompoundIndex(name = "tipo", def = "{'tipo' : 1}"),
  @CompoundIndex(name = "status", def = "{'status' : 1}"),
  @CompoundIndex(name = "nome-usuarionome", def = "{'nome' : 1, 'usuario.nome' : 1}"),
  @CompoundIndex(name = "nome-usuarioemail", def = "{'nome' : 1, 'usuario.email' : 1}")
})
@ToString
public class Lancamento implements Serializable {

  public static final long serialVersionUID = 1L;

  @Id private String id;

  @NotNull private UUID uuid;

  @NotNull private TituloLancamento nome;

  @NotNull private CentroCusto centroCustoPrimario;

  @NotNull private CentroCusto centroCustoSecundario;

  @NotNull private LocalDate vencimento;

  @NotNull private BigDecimal valorParcela;

  @JsonIgnore private int quantidadeTotalParcelas;

  @NotNull private int parcela;

  private boolean gerarParcelaUnica;

  private ContaBancaria contaBancaria;

  private String observacao;

  @NotNull private Status status;

  @NotNull private Tipo tipo;

  @NotNull private Usuario usuario;

  private Baixa baixa;

  @NotEmpty private LocalDateTime dataInclusao;

  private LocalDateTime dataAlteracao;

  private LocalDateTime dataExclusao;

  public Lancamento() {
    this.tipo = Tipo.DESPESA;
  }

  public Lancamento(
      final String id,
      final UUID uuid,
      final TituloLancamento nome,
      final CentroCusto centroCustoPrimario,
      final CentroCusto centroCustoSecundario,
      final LocalDate vencimento,
      final BigDecimal valorParcela,
      final int quantidadeTotalParcelas,
      final int parcela,
      final boolean gerarParcelaUnica,
      final ContaBancaria contaBancaria,
      final String observacao,
      final Status status,
      final Tipo tipo,
      final Usuario usuario,
      final Baixa baixa) {
    this.id = id;
    this.uuid = uuid;
    this.nome = nome;
    this.centroCustoPrimario = centroCustoPrimario;
    this.centroCustoSecundario = centroCustoSecundario;
    this.vencimento = vencimento;
    this.valorParcela = valorParcela;
    this.quantidadeTotalParcelas = quantidadeTotalParcelas;
    this.parcela = parcela;
    this.gerarParcelaUnica = gerarParcelaUnica;
    this.contaBancaria = contaBancaria;
    this.observacao = observacao;
    this.status = status;
    this.tipo = tipo;
    this.usuario = usuario;
    this.baixa = baixa;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public TituloLancamento getNome() {
    return nome;
  }

  public void setNome(TituloLancamento nome) {
    this.nome = nome;
  }

  public CentroCusto getCentroCustoPrimario() {
    return centroCustoPrimario;
  }

  public void setCentroCustoPrimario(CentroCusto centroCustoPrimario) {
    this.centroCustoPrimario = centroCustoPrimario;
  }

  public CentroCusto getCentroCustoSecundario() {
    return centroCustoSecundario;
  }

  public void setCentroCustoSecundario(CentroCusto centroCustoSecundario) {
    this.centroCustoSecundario = centroCustoSecundario;
  }

  public LocalDate getVencimento() {
    return vencimento;
  }

  public void setVencimento(LocalDate vencimento) {
    this.vencimento = vencimento;
  }

  public BigDecimal getValorParcela() {
    return valorParcela;
  }

  public void setValorParcela(BigDecimal valorParcela) {
    this.valorParcela = valorParcela;
  }

  public int getQuantidadeTotalParcelas() {
    return quantidadeTotalParcelas;
  }

  public void setQuantidadeTotalParcelas(int quantidadeTotalParcelas) {
    this.quantidadeTotalParcelas = quantidadeTotalParcelas;
  }

  public int getParcela() {
    return parcela;
  }

  public void setParcela(int parcela) {
    this.parcela = parcela;
  }

  public boolean isGerarParcelaUnica() {
    return gerarParcelaUnica;
  }

  public void setGerarParcelaUnica(boolean gerarParcelaUnica) {
    this.gerarParcelaUnica = gerarParcelaUnica;
  }

  public String getParcelaAtualTotalParcela() {
    return this.parcela + "/" + this.quantidadeTotalParcelas;
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

  public Baixa getBaixa() {
    return baixa;
  }

  public void setBaixa(Baixa baixa) {
    this.baixa = baixa;
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
  public String toString() {
    NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    final StringBuilder sb = new StringBuilder("Lancamento{");
    sb.append("Vencimento: ");
    sb.append(getVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    sb.append(", Descrição: ");
    sb.append(getNome().getNome());
    sb.append(", Valor: ");
    sb.append(nf.format(getValorParcela()));
    sb.append(", Parcela: ");
    sb.append(getParcelaAtualTotalParcela());
    sb.append(", Observação: ");
    sb.append(getObservacao());
    sb.append("\n");
    return sb.toString();
  }
}
