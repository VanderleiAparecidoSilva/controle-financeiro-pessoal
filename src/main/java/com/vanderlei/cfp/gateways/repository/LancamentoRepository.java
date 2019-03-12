package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LancamentoRepository extends MongoRepository<Lancamento, String> {

  @Transactional(readOnly = true)
  Optional<Lancamento> findByIdAndUsuarioEmail(final String id, final String email);

  @Transactional(readOnly = true)
  Collection<Lancamento> findByStatusAndVencimentoBeforeOrderByUsuarioNome(
      final Status status, final LocalDate date);

  @Transactional(readOnly = true)
  Page<Lancamento> findByStatusAndUsuarioEmailAndUuidAndParcelaGreaterThanOrderByParcela(
      final Status status,
      final String email,
      final UUID uuid,
      final int parcela,
      final Pageable pageable);

  @Transactional(readOnly = true)
  Page<Lancamento> findByTipoAndUsuarioEmail(
      final Tipo tipo, final String email, final Pageable pageable);

  @Transactional(readOnly = true)
  Page<Lancamento> findByTipoAndStatusAndUsuarioEmailAndDataExclusaoIsNullAndVencimentoBetween(
      final Tipo tipo,
      final Status status,
      final String email,
      final LocalDateTime from,
      final LocalDateTime to,
      final Pageable pageable);

  @Transactional(readOnly = true)
  Page<Lancamento> findByNomeNomeLikeIgnoreCaseAndTipoAndStatusAndUsuarioEmailAndDataExclusaoIsNullAndVencimentoBetween(
      final String nome,
      final Tipo tipo,
      final Status status,
      final String email,
      final LocalDateTime from,
      final LocalDateTime to,
      final Pageable pageable);

  @Transactional(readOnly = true)
  Optional<Lancamento> findFirstByNomeNomeLikeIgnoreCaseAndTipoAndVencimentoAndObservacaoAndUsuarioEmail(
      final String nome, final Tipo tipo, final LocalDate vencimento, final String observacao, final String email);
}
