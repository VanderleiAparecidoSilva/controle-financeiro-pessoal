package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Baixa;
import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.converters.LancamentoConverter;
import com.vanderlei.cfp.gateways.repository.LancamentoRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class LancamentoGateway {

  private final String msgObjectNotFound = "Lançamento não encontrado! Codigo: ";

  private final String msgUsuarioObjectNotFound = "O usuário informado no lançamento não existe: ";

  private final String msgContaBancariaObjectNotFound = "A conta bancária informada não existe: ";

  private final String msgBaixaUserNotFound =
      "O usuário informado na baixa do lançamento não corresponde ao lançamento informado: Título: ";

  private final String msgLancamentoEmAberto =
      "O lançamento informado está em aberto. Não será possível estorná-lo: ";

  private final String msgLancamentoFechado =
      "O lançamento informado está fechado. Não será possível baixa-lo: ";

  private final String msgTipo = ", Tipo: ";

  @Autowired private LancamentoRepository repository;

  @Autowired private UsuarioGateway usuarioGateway;

  @Autowired private TituloLancamentoGateway tituloLancamentoGateway;

  @Autowired private CentroCustoGateway centroCustoGateway;

  @Autowired private ContaBancariaGateway contaBancariaGateway;

  @Autowired private LancamentoConverter lancamentoConverter;

  public Page<Lancamento> buscarTodosPorUsuarioPaginado(
      final Tipo tipo,
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
    return repository.findByTipoAndUsuarioEmail(tipo, objSecurity.getUsername(), pageRequest);
  }

  public Page<Lancamento> buscarTodosPorPeriodoUsuarioPaginado(
      final LocalDate from,
      final LocalDate to,
      final Tipo tipo,
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

    return repository.findByTipoAndUsuarioEmailAndVencimentoBetween(
        tipo,
        objSecurity.getUsername(),
        from.minusDays(1).atTime(23, 59, 59),
        to.plusDays(1).atStartOfDay(),
        pageRequest);
  }

  public Lancamento buscarPorCodigo(final String id) {
    UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
    if (objSecurity == null) {
      throw new AuthorizationException("Acesso negado");
    }
    Optional<Lancamento> obj = repository.findById(id);
    Lancamento lancamento =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + Lancamento.class.getName()));
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(lancamento.getUsuario().getEmail())) {
      return lancamento;
    }

    return null;
  }

  public Page<Lancamento> buscarParcelasLancamentoAbertoPaginado(
      final String id,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
    if (objSecurity == null) {
      throw new AuthorizationException("Acesso negado");
    }
    Lancamento obj = this.buscarPorCodigo(id);
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      PageRequest pageRequest =
          PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
      Page<Lancamento> objList =
          repository.findByStatusAndUsuarioEmailAndUuidAndParcelaGreaterThanOrderByParcela(
              Status.ABERTO,
              obj.getUsuario().getEmail(),
              obj.getUuid(),
              obj.getParcela(),
              pageRequest);
      return objList;
    }

    return null;
  }

  public Lancamento inserir(final Lancamento obj) {
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      Lancamento objRet = null;

      if (obj.getUsuario() != null
          && !usuarioGateway
              .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
              .isPresent()) {
        throw new ObjectNotFoundException(
            msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + Lancamento.class.getName());
      }
      if (obj.getNome() != null
          && !tituloLancamentoGateway
              .buscarPorNomeUsuarioEmail(obj.getNome().getNome(), obj.getUsuario().getEmail())
              .isPresent()) {
        if (obj.getTipo().equals(Tipo.RECEITA)) {
          obj.getNome().setAplicarNaReceita(true);
        } else if (obj.getTipo().equals(Tipo.DESPESA)) {
          obj.getNome().setAplicarNaDespesa(true);
        }
        obj.getNome().setDataInclusao(LocalDateTime.now());
        obj.getNome().setDiaVencimento(obj.getVencimento().getDayOfMonth());
        tituloLancamentoGateway.salvar(obj.getNome());
      }
      if (obj.getCentroCusto() != null
          && !centroCustoGateway
              .buscarPorNomeUsuarioEmail(
                  obj.getCentroCusto().getNome(), obj.getUsuario().getEmail())
              .isPresent()) {
        if (obj.getTipo().equals(Tipo.RECEITA)) {
          obj.getCentroCusto().setAplicarNaReceita(true);
        } else if (obj.getTipo().equals(Tipo.DESPESA)) {
          obj.getCentroCusto().setAplicarNaDespesa(true);
        }
        obj.getCentroCusto().setDataInclusao(LocalDateTime.now());
        centroCustoGateway.salvar(obj.getCentroCusto());
      }
      if (obj.getContaBancaria() != null
          && !contaBancariaGateway
              .buscarPorNomeUsuarioEmail(
                  obj.getContaBancaria().getNome(), obj.getUsuario().getEmail())
              .isPresent()) {
        throw new ObjectNotFoundException(
            msgContaBancariaObjectNotFound
                + obj.getContaBancaria().getNome()
                + msgTipo
                + Lancamento.class.getName());
      }

      final int qtdParcelas = obj.getQuantidadeTotalParcelas();
      LocalDate vencimento = obj.getVencimento();
      final UUID uuid = UUID.randomUUID();
      for (int i = 0; i < qtdParcelas; i++) {
        obj.setId(null);
        obj.setUuid(uuid);
        obj.setDataInclusao(LocalDateTime.now());
        if (!obj.isGerarParcelaUnica()) {
          obj.setParcela(i + 1);
        } else {
          obj.setQuantidadeTotalParcelas(1);
          obj.setParcela(1);
        }
        if (i > 0) {
          obj.setVencimento(vencimento.plusMonths(1));
          vencimento = vencimento.plusMonths(1);
        }

        repository.save(obj);
        if (objRet == null) {
          objRet = obj;
        }
      }

      return objRet;
    }

    return null;
  }

  public void alterarTipo(final String id) {
    Lancamento obj = this.buscarPorCodigo(id);
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      obj.setTipo(obj.getTipo().equals(Tipo.RECEITA) ? Tipo.DESPESA : Tipo.RECEITA);
      repository.save(obj);
    }
  }

  public void baixar(final String id, final Baixa baixa) {
    Lancamento obj = this.buscarPorCodigo(id);
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      if (obj.getStatus().equals(Status.PAGO)) {
        throw new ObjectNotFoundException(
            msgLancamentoFechado
                + obj.getNome().getNome()
                + ", status: "
                + obj.getStatus().getDescricao()
                + msgTipo
                + Lancamento.class.getName());
      }
      if (!obj.getUsuario().getEmail().equals(baixa.getUsuario().getEmail())
          || !obj.getUsuario().getNome().equals(baixa.getUsuario().getNome())) {
        throw new ObjectNotFoundException(
            msgBaixaUserNotFound
                + obj.getNome().getNome()
                + ", usuário: "
                + baixa.getUsuario()
                + msgTipo
                + Lancamento.class.getName());
      }
      if (obj.getContaBancaria() != null
          && !contaBancariaGateway
              .buscarPorNomeUsuarioEmail(
                  obj.getContaBancaria().getNome(), obj.getUsuario().getEmail())
              .isPresent()) {
        throw new ObjectNotFoundException(
            msgContaBancariaObjectNotFound
                + obj.getContaBancaria().getNome()
                + msgTipo
                + Lancamento.class.getName());
      }
      obj.setBaixa(baixa);
      obj.setStatus(obj.getTipo().equals(Tipo.RECEITA) ? Status.RECEBIDO : Status.PAGO);
      repository.save(obj);

      if (UsuarioSecurityGateway.userAuthenticatedByEmail(baixa.getUsuario().getEmail())) {
        if (baixa.getContaBancaria() != null) {
          contaBancariaGateway
              .buscarPorNomeUsuarioEmail(
                  baixa.getContaBancaria().getNome(), baixa.getUsuario().getEmail())
              .ifPresent(
                  contaBancaria -> {
                    if (contaBancaria.getAtualizarSaldoBancarioNaBaixaTitulo()) {
                      contaBancariaGateway.atualizarSaldo(
                          contaBancaria.getId(), obj.getValorParcela(), Operacao.DEBITO);
                    }
                  });
        }
      }
    }
  }

  public void estornar(final String id, final Baixa baixa) {
    Lancamento obj = this.buscarPorCodigo(id);
    if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
      if (obj.getStatus().equals(Status.ABERTO)) {
        throw new ObjectNotFoundException(
            msgLancamentoEmAberto
                + obj.getNome().getNome()
                + ", status: "
                + obj.getStatus().getDescricao()
                + msgTipo
                + Lancamento.class.getName());
      }
      if (!obj.getUsuario().getEmail().equals(baixa.getUsuario().getEmail())
          || !obj.getUsuario().getNome().equals(baixa.getUsuario().getNome())) {
        throw new ObjectNotFoundException(
            msgBaixaUserNotFound
                + obj.getNome().getNome()
                + ", usuário: "
                + baixa.getUsuario()
                + msgTipo
                + Lancamento.class.getName());
      }
      obj.setBaixa(null);
      obj.setStatus(Status.ABERTO);
      repository.save(obj);

      if (UsuarioSecurityGateway.userAuthenticatedByEmail(baixa.getUsuario().getEmail())) {
        if (baixa.getContaBancaria() != null) {
          contaBancariaGateway
              .buscarPorNomeUsuarioEmail(
                  baixa.getContaBancaria().getNome(), baixa.getUsuario().getEmail())
              .ifPresent(
                  contaBancaria -> {
                    if (contaBancaria.getAtualizarSaldoBancarioNaBaixaTitulo()) {
                      contaBancariaGateway.atualizarSaldo(
                          contaBancaria.getId(), obj.getValorParcela(), Operacao.CREDITO);
                    }
                  });
        }
      }
    }
  }

  public void ativar(final String id) {
    Lancamento obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(null);
    repository.save(obj);
  }

  public void desativar(final String id) {
    Lancamento obj = this.buscarPorCodigo(id);
    obj.setDataExclusao(LocalDateTime.now());
    repository.save(obj);
  }

  public Collection<Lancamento> buscarLancamentosVencidos(
      final Status status, final LocalDate date) {
    return repository.findByStatusAndVencimentoBeforeOrderByUsuarioNome(status, date);
  }
}
