package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.entities.enums.TipoUpload;
import com.vanderlei.cfp.entities.upload.CentroCustoUpload;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.CentroCustoRepository;
import com.vanderlei.cfp.gateways.repository.UploadRepository;
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
public class CentroCustoGateway {

  private final String msgObjectNotFound = "Centro de custo não encontrado! Codigo: ";

  private final String msgUsuarioObjectNotFound =
      "O usuário informado no centro de custo não existe: ";

  private final String msgObjectDuplicated = "Centro de custo já cadastrado com o nome: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private CentroCustoRepository repository;

  @Autowired private UploadRepository uploadRepository;

  @Autowired private UsuarioGateway usuarioGateway;

  public Page<CentroCusto> buscarTodosPorUsuarioPaginado(
      final String email,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findByUsuarioEmail(email, pageRequest);
  }

  public Page<CentroCusto> buscarTodosAtivosPorUsuarioPaginado(
      final String email,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    List<CentroCusto> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<CentroCusto> objPage = repository.findByUsuarioEmail(email, pageRequest);
    objPage.forEach(
        obj -> {
          if (obj.getAtivo()) {
            objList.add(obj);
          }
        });
    return new PageImpl<CentroCusto>(
        objList,
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy),
        objList.size());
  }

  public CentroCusto buscarPorCodigoUsuario(final String id, final String email) {
    Optional<CentroCusto> obj = repository.findByIdAndUsuarioEmail(id, email);
    CentroCusto centroCusto =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + CentroCusto.class.getName()));
    return centroCusto;
  }

  public Optional<CentroCusto> buscarPorNomeUsuarioEmail(final String nome, final String email) {
    return repository.findByNomeAndUsuarioEmail(nome, email);
  }

  public Page<CentroCusto> buscarPorNomeLikeUsuarioEmail(
      final String email,
      final String nome,
      final Boolean ativo,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    List<CentroCusto> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<CentroCusto> objPage = repository.findByNomeLikeIgnoreCaseAndUsuarioEmail(nome, email, pageRequest);
    if (ativo) {
      objPage.forEach(
          obj -> {
            if (obj.getAtivo()) {
              objList.add(obj);
            }
          });
      return new PageImpl<CentroCusto>(
          objList,
          PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy),
          objList.size());
    } else {
      return objPage;
    }
  }

  public CentroCusto inserir(final CentroCusto obj) {
    if (!usuarioGateway
        .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + CentroCusto.class.getName());
    }
    if (repository
        .findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectDuplicatedException(
          msgObjectDuplicated + obj.getNome() + msgTipo + CentroCusto.class.getName());
    }
    obj.setId(null);
    obj.setDataInclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public CentroCusto atualizar(final CentroCusto obj) {
    if (!usuarioGateway
        .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
        .isPresent()) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + CentroCusto.class.getName());
    }
    obj.setDataAlteracao(LocalDateTime.now());
    return repository.save(obj);
  }

  public CentroCusto ativar(final String id, final String email) {
    CentroCusto obj = this.buscarPorCodigoUsuario(id, email);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public CentroCusto desativar(final String id, final String email) {
    CentroCusto obj = this.buscarPorCodigoUsuario(id, email);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public void salvar(final CentroCusto obj) {
    repository.save(obj);
  }

  public void upload(final String email, final String str) {
    Usuario usuario = usuarioGateway.buscarPorEmail(email, true);

    if (usuario != null) {
      CentroCustoUpload obj = new CentroCustoUpload();
      String[] strArray = str.split(";");

      obj.setTipo(TipoUpload.toEnum(strArray[0]));
      obj.setNome(strArray[1]);
      obj.setAplicarNaReceita(Boolean.valueOf(strArray[2]));
      obj.setAplicarNaDespesa(Boolean.valueOf(strArray[3]));

      obj.setId(null);
      obj.setDataInclusao(LocalDateTime.now());
      obj.setUsuario(usuario);
      obj.setProcessado(false);
      uploadRepository.save(obj);
    }
  }
}
