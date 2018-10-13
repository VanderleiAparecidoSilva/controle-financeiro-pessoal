package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
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

  @Value("${img.prefix.client.profile}")
  private String prefix;

  @Value("${img.profile.size}")
  private Integer size;

  public Usuario buscarPorCodigo(final String id) {
    if (UsuarioSecurityGateway.userAuthenticatedById(id)) {
      Optional<Usuario> obj = repository.findById(id);
      return obj.orElseThrow(
          () ->
              new ObjectNotFoundException(
                  msgObjectNotFoundUsuarioCodigo + id + msgTipo + Usuario.class.getName()));
    }

    return null;
  }

  public Usuario buscarPorEmail(final String email) {
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(email)) {
      Optional<Usuario> obj = repository.findByEmail(email)
              .filter(usuario -> usuario.getAtivo());
      return obj.orElseThrow(
          () ->
              new ObjectNotFoundException(
                  msgObjectNotFoundUsuarioEmail + email + msgTipo + Usuario.class.getName()));
    }

    return null;
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
    return repository.save(obj);
  }

  public Usuario atualizar(final Usuario obj) {
    if (UsuarioSecurityGateway.userAuthenticatedById(obj.getId())) {
      obj.setDataAlteracao(LocalDateTime.now());
      return repository.save(obj);
    }

    return null;
  }

  public Usuario ativar(final String id) {
    Usuario obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public Usuario desativar(final String id) {
    Usuario obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public URI atualizarFotoPessoal(MultipartFile multipartFile) {
    UsuarioSecurity usuarioSecurity = UsuarioSecurityGateway.authenticated();
    if (usuarioSecurity == null) {
      throw new AuthorizationException("Acesso negado");
    }

    BufferedImage jpgImage = imageGateway.getJpgImageFromFile(multipartFile);
    String fileName = prefix + usuarioSecurity.getId() + ".jpg";

    return s3Gateway.uploadFile(
        imageGateway.getInputStream(imageGateway.cropAndResize(jpgImage, size), "jpg"),
        fileName,
        "image");
  }
}
