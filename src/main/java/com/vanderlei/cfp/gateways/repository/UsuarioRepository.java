package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByNomeAndEmail(final String nome, final String email);

    Collection<Usuario> findByEmail(final String email);
}