package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.TituloLancamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TituloLancamentoRepository extends MongoRepository<TituloLancamento, String> {

    Optional<TituloLancamento> findByNomeAndUsuarioEmail(final String nome, final String email);

    Collection<TituloLancamento> findByUsuarioEmail(final String email);
}