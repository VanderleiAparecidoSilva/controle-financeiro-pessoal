package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.ContaBancaria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ContaBancariaRepository extends MongoRepository<ContaBancaria, String> {

    Optional<ContaBancaria> findByNomeAndUsuarioEmail(final String nome, final String email);

    Collection<ContaBancaria> findByUsuarioEmail(final String email);
}