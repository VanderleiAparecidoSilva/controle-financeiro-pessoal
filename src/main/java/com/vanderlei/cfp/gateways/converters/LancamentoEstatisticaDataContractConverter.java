package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.gateways.CentroCustoGateway;
import com.vanderlei.cfp.http.data.LancamentoEstatisticaCentroCustoDataContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class LancamentoEstatisticaDataContractConverter
    implements Converter<Map.Entry<?, ?>, LancamentoEstatisticaCentroCustoDataContract> {

  @Autowired private CentroCustoGateway gateway;

  @Autowired private CentroCustoDataContractConverter converter;

  @Override
  public LancamentoEstatisticaCentroCustoDataContract convert(Map.Entry<?, ?> map) {
    return new LancamentoEstatisticaCentroCustoDataContract(
        converter.convert(gateway.buscarPorNome(String.valueOf(map.getKey()))),
        (BigDecimal) map.getValue());
  }
}
