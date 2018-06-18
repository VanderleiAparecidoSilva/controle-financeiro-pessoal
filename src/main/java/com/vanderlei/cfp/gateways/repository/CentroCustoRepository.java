package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.CentroCusto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CentroCustoRepository extends MongoRepository<CentroCusto, String> {

    Optional<CentroCusto> findByNomeAndUsuarioEmail(final String nome, final String email);

    Collection<CentroCusto> findByUsuarioEmail(final String email);
}