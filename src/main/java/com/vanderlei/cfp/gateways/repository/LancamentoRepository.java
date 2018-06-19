package com.vanderlei.cfp.gateways.repository;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LancamentoRepository extends MongoRepository<Lancamento, String> {

    Optional<Lancamento> findByNomeAndUsuarioEmail(final String nome, final String email);

    Collection<Lancamento> findByStatusAndVencimentoBeforeOrderByUsuarioNome(final Status status, final LocalDate date);

    Collection<Lancamento> findByStatusAndUsuarioEmailAndUuidAndParcelaGreaterThanOrderByParcela(final Status status,
                                                                                                 final String email,
                                                                                                 final UUID uuid,
                                                                                                 final int parcela);

    Collection<Lancamento> findByUsuarioEmail(final String email);
}