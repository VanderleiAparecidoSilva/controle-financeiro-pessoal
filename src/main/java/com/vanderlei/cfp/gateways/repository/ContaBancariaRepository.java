package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.ContaBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ContaBancariaRepository extends MongoRepository<ContaBancaria, String> {

  @Transactional(readOnly = true)
  Optional<ContaBancaria> findByNomeAndUsuarioEmail(final String nome, final String email);

  @Transactional(readOnly = true)
  Page<ContaBancaria> findByUsuarioEmail(final String email, final Pageable pageable);
}
