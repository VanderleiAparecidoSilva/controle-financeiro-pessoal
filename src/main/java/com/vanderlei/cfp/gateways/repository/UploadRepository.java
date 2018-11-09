package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.upload.Upload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends MongoRepository<Upload, String> {}
