package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.TituloReceitaDespesa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TituloReceitaDespesaRepository extends MongoRepository<TituloReceitaDespesa, String> {

    Optional<TituloReceitaDespesa> findByNomeAndUsuarioEmail(final String nome, final String email);
}