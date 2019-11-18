package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.upload.UploadBank;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Profile("!test")
public interface UploadBankRepository extends MongoRepository<UploadBank, String> {

  @Transactional(readOnly = true)
  Optional<UploadBank> findByIdAndUsuarioEmail(final String id, final String email);
}
