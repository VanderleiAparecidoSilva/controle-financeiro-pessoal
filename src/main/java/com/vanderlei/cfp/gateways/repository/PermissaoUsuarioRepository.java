package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.PermissaoUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissaoUsuarioRepository extends MongoRepository<PermissaoUsuario, String> {

  @Transactional(readOnly = true)
  Optional<PermissaoUsuario> findByIdUsuarioAndIdPermissao(
      final String idUsuario, final String idPermissao);

  @Transactional(readOnly = true)
  List<PermissaoUsuario> findByIdUsuario(final String idUsuario);
}
