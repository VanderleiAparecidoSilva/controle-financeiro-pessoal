package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.entities.PermissaoUsuario;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.PermissaoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PermissaoUsuarioGateway {

  private final String msgObjectNotFound = "Permissão do usuário não encontrada! Codigo: ";

  private final String msgObjectDuplicated = "Permissão já vinculada ao usuário com ID: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private PermissaoUsuarioRepository repository;

  public PermissaoUsuario buscarPorCodigo(final String id) {
    Optional<PermissaoUsuario> obj = repository.findById(id);
    PermissaoUsuario permissaoUsuario =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + Permissao.class.getName()));
    return permissaoUsuario;
  }

  public PermissaoUsuario inserir(final PermissaoUsuario obj) {
    if (repository
        .findByIdUsuarioAndIdPermissao(obj.getIdUsuario(), obj.getIdPermissao())
        .isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicated + obj.getIdUsuario() + msgTipo + PermissaoUsuario.class.getName());
    }
    obj.setId(null);
    obj.setDataInclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public PermissaoUsuario atualizar(final PermissaoUsuario obj) {
    obj.setDataAlteracao(LocalDateTime.now());
    return repository.save(obj);
  }

  public PermissaoUsuario ativar(final String id) {
    PermissaoUsuario obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public PermissaoUsuario desativar(final String id) {
    PermissaoUsuario obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public void salvar(final PermissaoUsuario obj) {
    repository.save(obj);
  }
}
