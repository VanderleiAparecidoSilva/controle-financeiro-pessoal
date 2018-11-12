package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioGateway {

  private final String msgObjectNotFoundUsuarioCodigo = "Usuario não encontrado! Codigo: ";

  private final String msgObjectNotFoundUsuarioEmail = "Usuario não encontrado! Email: ";

  private final String msgObjectDuplicatedUsuarioNome = "Usuario já cadastrado com o nome: ";

  private final String msgObjectDuplicatedUsuarioEmail = "Usuario já cadastrado com o email: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private S3Gateway s3Gateway;

  @Autowired private ImageGateway imageGateway;

  @Autowired private UsuarioRepository repository;

  @Autowired private PermissaoUsuarioGateway permissaoUsuarioGateway;

  @Value("${img.prefix.client.profile}")
  private String prefix;

  @Value("${img.profile.size}")
  private Integer size;

  public Usuario buscarPorCodigo(final String id) {
    Optional<Usuario> obj = repository.findById(id);
    return obj.orElseThrow(
        () ->
            new ObjectNotFoundException(
                msgObjectNotFoundUsuarioCodigo + id + msgTipo + Usuario.class.getName()));
  }

  public Usuario buscarPorEmail(final String email, final boolean active) {
    Optional<Usuario> obj = repository.findByEmail(email);
    if (active) {
      obj = obj.filter(usuario -> usuario.getAtivo());
    }
    return obj.orElseThrow(
        () ->
            new ObjectNotFoundException(
                msgObjectNotFoundUsuarioEmail + email + msgTipo + Usuario.class.getName()));
  }

  public Optional<Usuario> buscarPorNomeEmail(final String nome, final String email) {
    return repository.findByNomeAndEmail(nome, email);
  }

  public Usuario inserir(final Usuario obj) {
    if (repository.findByNomeAndEmail(obj.getNome(), obj.getEmail()).isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicatedUsuarioNome + obj.getNome() + msgTipo + Usuario.class.getName());
    } else if (repository.findByEmail(obj.getEmail()).isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicatedUsuarioEmail + obj.getEmail() + msgTipo + Usuario.class.getName());
    }
    obj.setId(null);
    obj.setDataInclusao(LocalDateTime.now());
    Usuario objRet = repository.save(obj);
    if (permissaoUsuarioGateway.buscarPorUsuario(objRet.getId()).size() == 0) {
      permissaoUsuarioGateway.inserirPermissaoPadraoParaUsuario(objRet.getId());
    }
    return objRet;
  }

  public Usuario atualizar(final Usuario obj) {
    obj.setDataAlteracao(LocalDateTime.now());
    if (permissaoUsuarioGateway.buscarPorUsuario(obj.getId()).size() == 0) {
      permissaoUsuarioGateway.inserirPermissaoPadraoParaUsuario(obj.getId());
    }
    return repository.save(obj);
  }

  public Usuario ativar(final String email) {
    Usuario obj = this.buscarPorEmail(email, false);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public Usuario desativar(final String email) {
    Usuario obj = this.buscarPorEmail(email, true);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }
}
