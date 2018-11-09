package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.upload.Upload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface UploadRepository extends MongoRepository<Upload, String> {
  @Transactional(readOnly = true)
  Collection<Upload> findByProcessado(final Boolean processado);
}
