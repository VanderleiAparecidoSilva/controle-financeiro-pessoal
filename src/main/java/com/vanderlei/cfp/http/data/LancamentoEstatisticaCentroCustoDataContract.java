package com.vanderlei.cfp.http.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class LancamentoEstatisticaCentroCustoDataContract implements Serializable {

  private static final long serialVersionUID = 1L;

  private CentroCustoDataContract centroCusto;

  private BigDecimal total;

  public LancamentoEstatisticaCentroCustoDataContract(
      final CentroCustoDataContract centroCusto, final BigDecimal total) {
    this.centroCusto = centroCusto;
    this.total = total;
  }

  public CentroCustoDataContract getCentroCusto() {
    return centroCusto;
  }

  public void setCentroCusto(CentroCustoDataContract centroCusto) {
    this.centroCusto = centroCusto;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
