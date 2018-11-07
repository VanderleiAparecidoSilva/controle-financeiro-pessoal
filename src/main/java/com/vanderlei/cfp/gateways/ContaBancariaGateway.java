package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaBancariaGateway {

  private final String msgObjectNotFound = "Conta bancária não encontrada! Codigo: ";

  private final String msgUsuarioObjectNotFound =
      "O usuário informado na conta bancária não existe: ";

  private final String msgObjectDuplicated = "Conta bancária já cadastrada com o nome: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private ContaBancariaRepository repository;

  @Autowired private UsuarioGateway usuarioGateway;

  public Page<ContaBancaria> buscarTodosPorUsuarioPaginado(
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findByUsuarioEmail("", pageRequest);
  }

  public Page<ContaBancaria> buscarTodosAtivosPorUsuarioPaginado(
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    List<ContaBancaria> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<ContaBancaria> objPage = repository.findByUsuarioEmail("", pageRequest);
    objPage.forEach(
        obj -> {
          if (obj.getAtivo()) {
            objList.add(obj);
          }
        });
    return new PageImpl<ContaBancaria>(
        objList,
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy),
        objList.size());
  }

  public Optional<ContaBancaria> buscarPorNomeUsuarioEmail(final String nome, final String email) {
    return repository.findByNomeAndUsuarioEmail(nome, email);
  }

  public ContaBancaria buscarPorCodigo(final String id) {
    Optional<ContaBancaria> obj = repository.findById(id);
    ContaBancaria contaBancaria =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + ContaBancaria.class.getName()));
    return contaBancaria;
  }

  public ContaBancaria inserir(final ContaBancaria obj) {
    if (!usuarioGateway
        .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + ContaBancaria.class.getName());
    }
    if (repository
        .findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicated + obj.getNome() + msgTipo + ContaBancaria.class.getName());
    }
    obj.setId(null);
    obj.setDataInclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public ContaBancaria atualizar(final ContaBancaria obj) {
    if (!usuarioGateway
        .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + ContaBancaria.class.getName());
    }
    obj.setDataAlteracao(LocalDateTime.now());
    return repository.save(obj);
  }

  public ContaBancaria ativar(final String id) {
    ContaBancaria obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public ContaBancaria desativar(final String id) {
    ContaBancaria obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public void atualizarSaldo(final String id, final Double valor, final Operacao operacao) {
    ContaBancaria obj = this.buscarPorCodigo(id);
    obj.setDataAlteracao(LocalDateTime.now());
    obj.setSaldoContaBancaria(
        operacao.equals(Operacao.CREDITO)
            ? (obj.getSaldoContaBancaria() + valor)
            : (obj.getSaldoContaBancaria() - valor));
    repository.save(obj);
  }
}
