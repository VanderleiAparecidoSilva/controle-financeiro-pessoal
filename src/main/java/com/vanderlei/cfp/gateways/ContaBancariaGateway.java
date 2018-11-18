package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.entities.enums.TipoUpload;
import com.vanderlei.cfp.entities.upload.ContaBancariaUpload;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.ContaBancariaRepository;
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
public class ContaBancariaGateway {

  private final String msgObjectNotFound = "Conta bancária não encontrada! Codigo: ";

  private final String msgUsuarioObjectNotFound =
      "O usuário informado na conta bancária não existe: ";

  private final String msgObjectDuplicated = "Conta bancária já cadastrada com o nome: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private ContaBancariaRepository repository;

  @Autowired private UploadRepository uploadRepository;

  @Autowired private UsuarioGateway usuarioGateway;

  public Page<ContaBancaria> buscarTodosPorUsuarioPaginado(
      final String email,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findByUsuarioEmail(email, pageRequest);
  }

  public Page<ContaBancaria> buscarTodosAtivosPorUsuarioPaginado(
      final String email,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    List<ContaBancaria> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<ContaBancaria> objPage = repository.findByUsuarioEmail(email, pageRequest);
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

  public Page<ContaBancaria> buscarPorNomeLikeUsuarioEmail(
      final String email,
      final String nome,
      final Boolean ativo,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    List<ContaBancaria> objList = new ArrayList<>();
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<ContaBancaria> objPage =
        repository.findByNomeLikeIgnoreCaseAndUsuarioEmail(nome, email, pageRequest);
    if (ativo) {
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
    } else {
      return objPage;
    }
  }

  public ContaBancaria buscarPorCodigoUsuario(final String id, final String email) {
    Optional<ContaBancaria> obj = repository.findByIdAndUsuarioEmail(id, email);
    ContaBancaria contaBancaria =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + ContaBancaria.class.getName()));
    return contaBancaria;
  }

  public ContaBancaria inserir(final ContaBancaria obj) {
    if (usuarioGateway.buscarPorEmail(obj.getUsuario().getEmail(), true) == null) {
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
    if (usuarioGateway.buscarPorEmail(obj.getUsuario().getEmail(), true) == null) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + ContaBancaria.class.getName());
    }
    obj.setDataAlteracao(LocalDateTime.now());
    return repository.save(obj);
  }

  public ContaBancaria ativar(final String id, final String email) {
    ContaBancaria obj = this.buscarPorCodigoUsuario(id, email);
    obj.setDataExclusao(null);
    return repository.save(obj);
  }

  public ContaBancaria desativar(final String id, final String email) {
    ContaBancaria obj = this.buscarPorCodigoUsuario(id, email);
    obj.setDataExclusao(LocalDateTime.now());
    return repository.save(obj);
  }

  public void atualizarSaldo(
      final String id, final String email, final Double valor, final Operacao operacao) {
    ContaBancaria obj = this.buscarPorCodigoUsuario(id, email);
    obj.setDataAlteracao(LocalDateTime.now());
    obj.setSaldoContaBancaria(
        operacao.equals(Operacao.CREDITO)
            ? (obj.getSaldoContaBancaria() + valor)
            : (obj.getSaldoContaBancaria() - valor));
    repository.save(obj);
  }

  public void upload(final String email, final String str) {
    Usuario usuario = usuarioGateway.buscarPorEmail(email, true);

    if (usuario != null) {
      ContaBancariaUpload obj = new ContaBancariaUpload();
      String[] strArray = str.split(";");

      obj.setTipo(TipoUpload.toEnum(strArray[0]));
      obj.setNome(strArray[1].toUpperCase());
      obj.setNumeroContaBancaria(strArray[2]);
      obj.setLimiteContaBancaria(Double.valueOf(strArray[3]));
      obj.setSaldoContaBancaria(Double.valueOf(strArray[4]));
      obj.setVincularSaldoBancarioNoTotalReceita(Boolean.valueOf(strArray[5]));
      obj.setAtualizarSaldoBancarioNaBaixaTitulo(Boolean.valueOf(strArray[6]));

      obj.setId(null);
      obj.setDataInclusao(LocalDateTime.now());
      obj.setUsuario(usuario);
      obj.setProcessado(false);
      uploadRepository.save(obj);
    }
  }
}
