package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.entities.PermissaoUsuario;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.PermissaoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Profile("!test")
public class PermissaoUsuarioGateway {

  private final String msgUsuarioObjectNotFound =
      "O usuário informado no centro de custo não existe: ";

  private final String msgObjectNotFound = "Permissão do usuário não encontrada! Codigo: ";

  private final String msgObjectDuplicated = "Permissão já vinculada ao usuário com ID: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private PermissaoUsuarioRepository repository;

  @Autowired private PermissaoGateway permissaoGateway;

  @Autowired private UsuarioGateway usuarioGateway;

  public PermissaoUsuario buscarPorCodigo(final String id) {
    Optional<PermissaoUsuario> obj = repository.findById(id);
    PermissaoUsuario permissaoUsuario =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + Permissao.class.getName()));
    return permissaoUsuario;
  }

  public List<PermissaoUsuario> buscarPorUsuario(final String userId) {
    return repository.findByIdUsuario(userId);
  }

  public PermissaoUsuario inserir(final PermissaoUsuario obj) {
    if (usuarioGateway.buscarPorCodigo(obj.getIdUsuario()) == null) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getIdUsuario() + msgTipo + CentroCusto.class.getName());
    }
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
    if (usuarioGateway.buscarPorCodigo(obj.getIdUsuario()) == null) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getIdUsuario() + msgTipo + CentroCusto.class.getName());
    }
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

  public void inserirPermissaoPadraoParaUsuario(final String userId) {
    List<Permissao> permissaoDefaultList = permissaoGateway.buscarDefault(true);
    permissaoDefaultList.forEach(
        o -> {
          PermissaoUsuario obj = new PermissaoUsuario();
          obj.setIdUsuario(userId);
          obj.setIdPermissao(o.getId());

          this.inserir(obj);
        });
  }
}
