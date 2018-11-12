package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.Permissao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissaoRepository extends MongoRepository<Permissao, String> {

  @Transactional(readOnly = true)
  Optional<Permissao> findByDescricao(final String descricao);

  @Transactional(readOnly = true)
  List<Permissao> findByPermissaoDefault(final Boolean value);
}
