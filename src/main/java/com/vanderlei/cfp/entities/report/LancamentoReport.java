package com.vanderlei.cfp.entities.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
public class LancamentoReport {

  private Date vencimento;

  private String nome;

  private BigDecimal valor;

  private String parcela;

  private String status;

  private String observacao;

  private BigDecimal total;
}
