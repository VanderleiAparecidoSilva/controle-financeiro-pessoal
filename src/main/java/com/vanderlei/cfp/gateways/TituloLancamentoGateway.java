package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.TituloLancamento;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.TituloLancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@Profile("!test")
public class TituloLancamentoGateway {

  private final String msgObjectNotFound =
      "Titulo de receita e/ou despesa não encontrado! Codigo: ";

  private final String msgUsuarioObjectNotFound =
      "O usuário informado no titulo da receita e/ou despesa não existe: ";

  private final String msgObjectDuplicated =
      "Titulo da receita e/ou despesa já cadastrado com o nome: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private TituloLancamentoRepository repository;

  @Autowired private UsuarioGateway usuarioGateway;

  public Page<TituloLancamento> buscarTodosPorUsuarioPaginado(
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findByUsuarioEmail("", pageRequest);
  }

  public Page<TituloLancamento> buscarTodosAtivosPorUsuarioPaginado(
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    List<TituloLancamento> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<TituloLancamento> objPage = repository.findByUsuarioEmail("", pageRequest);
    objPage.forEach(
        obj -> {
          if (obj.getAtivo()) {
            objList.add(obj);
          }
        });
    return new PageImpl<TituloLancamento>(
        objList,
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy),
        objList.size());
  }

  public TituloLancamento buscarPorCodigo(final String id) {
    Optional<TituloLancamento> obj = repository.findById(id);
    TituloLancamento tituloLancamento =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + TituloLancamento.class.getName()));
    return tituloLancamento;
  }

  public Optional<TituloLancamento> buscarPorNomeUsuarioEmail(
      final String nome, final String email) {
    return repository.findByNomeAndUsuarioEmail(nome, email);
  }

  public TituloLancamento inserir(final TituloLancamento obj) {
    if (usuarioGateway.buscarPorEmail(obj.getUsuario().getEmail(), true) == null) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + TituloLancamento.class.getName());
    }
    if (repository
        .findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicated + obj.getNome() + msgTipo + TituloLancamento.class.getName());
    }
    obj.setId(null);
    obj.setDataInclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public TituloLancamento atualizar(final TituloLancamento obj) {
    if (usuarioGateway.buscarPorEmail(obj.getUsuario().getEmail(), true) == null) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + TituloLancamento.class.getName());
    }
    obj.setDataAlteracao(LocalDateTime.now());
    return repository.save(obj);
  }

  public TituloLancamento ativar(final String id) {
    TituloLancamento obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public TituloLancamento desativar(final String id) {
    TituloLancamento obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public void salvar(final TituloLancamento obj) {
    repository.save(obj);
  }
}
