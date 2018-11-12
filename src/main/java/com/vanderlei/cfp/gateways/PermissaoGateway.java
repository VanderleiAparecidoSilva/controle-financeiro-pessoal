package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PermissaoGateway {

  private final String msgObjectNotFound = "Permissão não encontrada! Codigo: ";

  private final String msgObjectDuplicated = "Permissão já cadastrada com o nome: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private PermissaoRepository repository;

  public Permissao buscarPorCodigo(final String id) {
    Optional<Permissao> obj = repository.findById(id);
    Permissao permissao =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + Permissao.class.getName()));
    return permissao;
  }

  public Page<Permissao> buscarTodosPaginado(
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findAll(pageRequest);
  }

  public Permissao inserir(final Permissao obj) {
    if (repository.findByDescricao(obj.getDescricao()).isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicated + obj.getDescricao() + msgTipo + Permissao.class.getName());
    }
    obj.setId(null);
    obj.setDataInclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public Permissao atualizar(final Permissao obj) {
    obj.setDataAlteracao(LocalDateTime.now());
    return repository.save(obj);
  }

  public Permissao ativar(final String id) {
    Permissao obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public Permissao desativar(final String id) {
    Permissao obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public void salvar(final Permissao obj) {
    repository.save(obj);
  }

  public List<Permissao> buscarDefault(Boolean value) {
    return repository.findByPermissaoDefault(value);
  }
}
