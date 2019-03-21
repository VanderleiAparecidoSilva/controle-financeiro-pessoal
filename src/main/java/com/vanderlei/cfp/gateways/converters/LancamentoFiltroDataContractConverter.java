package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.LancamentoFiltro;
import com.vanderlei.cfp.http.data.LancamentoFiltroDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LancamentoFiltroDataContractConverter
    implements Converter<LancamentoFiltro, LancamentoFiltroDataContract> {

  @Override
  public LancamentoFiltroDataContract convert(LancamentoFiltro obj) {
    LancamentoFiltroDataContract objDataContract = new LancamentoFiltroDataContract();
    BeanUtils.copyProperties(obj, objDataContract);

    return objDataContract;
  }

  public List<LancamentoFiltroDataContract> convert(final List<LancamentoFiltro> objList) {
    return objList.stream().map(obj -> this.convert(obj)).collect(Collectors.toList());
  }
}
