package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.upload.UploadBank;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
public interface UploadBankRepository extends MongoRepository<UploadBank, String> {}
