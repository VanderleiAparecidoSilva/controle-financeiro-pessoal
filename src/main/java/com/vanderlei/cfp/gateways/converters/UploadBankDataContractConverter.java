package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.upload.UploadBank;
import com.vanderlei.cfp.http.data.UploadBankDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UploadBankDataContractConverter
    implements Converter<UploadBank, UploadBankDataContract> {

  @Override
  public UploadBankDataContract convert(UploadBank obj) {
    UploadBankDataContract objDataContract = new UploadBankDataContract();
    BeanUtils.copyProperties(obj, objDataContract);
    objDataContract.setUsuario(
        new UsuarioDataContract(
            null,
            obj.getUsuario().getNome(),
            obj.getUsuario().getEmail(),
            obj.getUsuario().getEmailCC(),
            null,
            obj.getUsuario().getPermiteEmailLembrete()));

    return objDataContract;
  }
}
