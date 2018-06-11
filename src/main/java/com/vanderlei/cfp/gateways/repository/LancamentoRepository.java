package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.Lancamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LancamentoRepository extends MongoRepository<Lancamento, String> {

    Optional<Lancamento> findByNomeAndUsuarioEmail(final String nome, final String email);
}