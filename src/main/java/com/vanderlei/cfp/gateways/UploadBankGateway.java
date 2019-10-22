package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.upload.UploadBank;
import com.vanderlei.cfp.gateways.repository.UploadBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadBankGateway {

  @Autowired private UploadBankRepository repository;

  public Optional<UploadBank> findById(final String id) {
    return repository.findById(id);
  }

  public void save(final UploadBank obj) {
    repository.save(obj);
  }
}
