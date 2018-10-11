package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.TituloLancamento;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.TituloLancamentoRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
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
    UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
    if (objSecurity == null) {
      throw new AuthorizationException("Acesso negado");
    }

    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findByUsuarioEmail(objSecurity.getUsername(), pageRequest);
  }

  public Page<TituloLancamento> buscarTodosAtivosPorUsuarioPaginado(
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
    if (objSecurity == null) {
      throw new AuthorizationException("Acesso negado");
    }

    List<TituloLancamento> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<TituloLancamento> objPage =
        repository.findByUsuarioEmail(objSecurity.getUsername(), pageRequest);
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
    UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
    if (objSecurity == null) {
      throw new AuthorizationException("Acesso negado");
    }
    Optional<TituloLancamento> obj = repository.findById(id);
    TituloLancamento tituloLancamento =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + TituloLancamento.class.getName()));
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(tituloLancamento.getUsuario().getEmail())) {
      return tituloLancamento;
    }

    return null;
  }

  public Optional<TituloLancamento> buscarPorNomeUsuarioEmail(
      final String nome, final String email) {
    return repository.findByNomeAndUsuarioEmail(nome, email);
  }

  public TituloLancamento inserir(final TituloLancamento obj) {
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      if (!usuarioGateway
          .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
          .isPresent()) {
        throw new ObjectNotFoundException(
            msgUsuarioObjectNotFound
                + obj.getUsuario()
                + msgTipo
                + TituloLancamento.class.getName());
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

    return null;
  }

  public TituloLancamento atualizar(final TituloLancamento obj) {
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      if (!usuarioGateway
          .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
          .isPresent()) {
        throw new ObjectNotFoundException(
            msgUsuarioObjectNotFound
                + obj.getUsuario()
                + msgTipo
                + TituloLancamento.class.getName());
      }
      obj.setDataAlteracao(LocalDateTime.now());
      return repository.save(obj);
    }

    return null;
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
