package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.upload.Upload;
import com.vanderlei.cfp.gateways.repository.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UploadGateway {

  @Autowired private UploadRepository repository;

  public Collection<Upload> buscarUploadsPendentes() {
    return repository.findByProcessadoAndErroIsNull(false);
  }

  public void save(final Upload obj) {
    repository.save(obj);
  }
}
