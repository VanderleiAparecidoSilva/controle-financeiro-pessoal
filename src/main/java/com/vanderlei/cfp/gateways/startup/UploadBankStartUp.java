package com.vanderlei.cfp.gateways.startup;

import com.vanderlei.cfp.config.json.JsonHelper;
import com.vanderlei.cfp.entities.upload.UploadBank;
import com.vanderlei.cfp.gateways.UploadBankGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class UploadBankStartUp {

  private final UploadBankGateway uploadBankGateway;

  private final JsonHelper jsonHelper;

  @PostConstruct
  public void init() {
    final List<UploadBank> uploadBankList =
        jsonHelper.toListObject("startup/uploadBankInfo.json", UploadBank.class);
    uploadBankList.stream()
        .filter(
            uploadBank ->
                Objects.isNull(
                    uploadBankGateway.findByIdAndUsuarioEmail(
                        uploadBank.getId(), uploadBank.getUsuario().getEmail())))
        .forEach(uploadBankGateway::save);
  }
}
