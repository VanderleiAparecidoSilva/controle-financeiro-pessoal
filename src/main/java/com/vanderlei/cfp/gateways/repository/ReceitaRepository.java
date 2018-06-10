package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.Receita;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceitaRepository extends MongoRepository<Receita, String> {

    Optional<Receita> findByNomeAndUsuarioEmail(final String nome, final String email);
}