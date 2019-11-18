package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.upload.UploadBank;
import com.vanderlei.cfp.gateways.repository.UploadBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("!test")
public class UploadBankGateway {

  @Autowired private UploadBankRepository repository;

  public UploadBank findByIdAndUsuarioEmail(final String id, final String email) {

    Optional<UploadBank> obj = repository.findByIdAndUsuarioEmail(id, email);
    if (obj.isPresent()) {
      return obj.get();
    }
    return null;
  }

  public void save(final UploadBank obj) {
    repository.save(obj);
  }
}
