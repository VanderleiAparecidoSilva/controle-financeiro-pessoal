package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.entities.enums.TipoUpload;
import com.vanderlei.cfp.entities.upload.LancamentoUpload;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.converters.LancamentoConverter;
import com.vanderlei.cfp.gateways.repository.LancamentoRepository;
import com.vanderlei.cfp.gateways.repository.UploadRepository;
import com.vanderlei.cfp.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LancamentoGateway {

  private static final String PATTERN_DATE_SHORT = "dd/MM/yy";

  private static final String PATTERN_DATE_LONG = "dd/MM/yyyy";

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

  @Autowired private UploadRepository uploadRepository;

  public Page<Lancamento> buscarTodosPorUsuarioPaginado(
      final String email,
      final Tipo tipo,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repository.findByTipoAndUsuarioEmail(tipo, email, pageRequest);
  }

  public Page<Lancamento> buscarTodosPorPeriodoUsuarioPaginado(
      final String email,
      final LocalDate from,
      final LocalDate to,
      final String description,
      final List<Status> status,
      final Tipo tipo,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction,
      final String orderByTwo,
      final String directionTwo) {

    List<Sort.Order> orders = new ArrayList<>();
    orders.add(new Sort.Order(Sort.Direction.valueOf(direction), orderBy));
    orders.add(new Sort.Order(Sort.Direction.valueOf(directionTwo), orderByTwo));

    PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.by(orders));

    if (StringUtils.isEmpty(description)) {
      return repository
          .findByTipoAndStatusInAndUsuarioEmailAndDataExclusaoIsNullAndVencimentoBetween(
              tipo,
              status,
              email,
              from.minusDays(1).atTime(23, 59, 59),
              to.plusDays(1).atStartOfDay(),
              pageRequest);
    } else {
      return repository
          .findByNomeNomeLikeIgnoreCaseAndTipoAndStatusInAndUsuarioEmailAndDataExclusaoIsNullAndVencimentoBetween(
              description,
              tipo,
              status,
              email,
              from.minusDays(1).atTime(23, 59, 59),
              to.plusDays(1).atStartOfDay(),
              pageRequest);
    }
  }

  public Lancamento buscarPorCodigoUsuarioEmail(final String id, final String email) {
    Optional<Lancamento> obj = repository.findByIdAndUsuarioEmail(id, email);
    Lancamento lancamento =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + Lancamento.class.getName()));
    return lancamento;
  }

  public Optional<Lancamento> buscarPorTituloTipoObservacaoUsuarioEmail(
      final String titulo,
      final Tipo tipo,
      final LocalDate vencimento,
      final String observacao,
      final String email) {
    return repository
        .findFirstByNomeNomeLikeIgnoreCaseAndTipoAndVencimentoAndObservacaoAndUsuarioEmail(
            titulo, tipo, vencimento, observacao, email);
  }

  public Lancamento buscarPorCodigo(final String id) {
    Optional<Lancamento> obj = repository.findById(id);
    Lancamento lancamento =
        obj.orElseThrow(
            () ->
                new ObjectNotFoundException(
                    msgObjectNotFound + id + msgTipo + Lancamento.class.getName()));
    return lancamento;
  }

  public Page<Lancamento> buscarParcelasLancamentoAbertoPaginado(
      final String id,
      final Integer page,
      final Integer linesPerPage,
      final String orderBy,
      final String direction) {
    Lancamento obj = this.buscarPorCodigo(id);
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

  public List<Lancamento> buscarEstatisticaCentroCusto(
      final Tipo tipo, final String email, final LocalDate from, final LocalDate to) {
    return repository.findByTipoAndUsuarioEmailAndDataExclusaoIsNullAndVencimentoBetween(
        tipo, email, from.minusDays(1).atTime(23, 59, 59), to.plusDays(1).atStartOfDay());
  }

  public List<LancamentoFiltro> buscarTodosAtivosPorUsuarioTipo(
      final String email, final String tipo) {
    final HashSet<LancamentoFiltro> filtros = new HashSet<>();
    if (tipo.equals("DESPESA")) {
      repository.findByTipoAndUsuarioEmailAndDataExclusaoIsNullOrderByNomeNome(Tipo.DESPESA, email)
          .stream()
          .map(l -> filtros.add(LancamentoFiltro.builder().nome(l.getNome().getNome()).build()))
          .collect(Collectors.toList());
    } else if (tipo.equals("RECEITA")) {
      repository.findByTipoAndUsuarioEmailAndDataExclusaoIsNullOrderByNomeNome(Tipo.RECEITA, email)
          .stream()
          .map(l -> filtros.add(LancamentoFiltro.builder().nome(l.getNome().getNome()).build()))
          .collect(Collectors.toList());
    }

    return filtros.stream()
        .sorted(Comparator.comparing(LancamentoFiltro::getNome))
        .collect(Collectors.toList());
  }

  public List<LancamentoFiltro> buscarTodosAtivosPorUsuario(final String email) {
    final HashSet<LancamentoFiltro> filtros = new HashSet<>();
    repository.findByUsuarioEmailAndDataExclusaoIsNullOrderByNomeNome(email).stream()
        .map(l -> filtros.add(LancamentoFiltro.builder().nome(l.getNome().getNome()).build()))
        .collect(Collectors.toList());

    return filtros.stream()
        .sorted(Comparator.comparing(LancamentoFiltro::getNome))
        .collect(Collectors.toList());
  }

  public Lancamento inserir(final Lancamento obj) {
    Lancamento objRet = null;

    if (obj.getUsuario() != null
        && !usuarioGateway
            .buscarPorNomeEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail())
            .isPresent()) {
      throw new ObjectNotFoundException(
          msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo + Lancamento.class.getName());
    }

    if (obj.getNome() != null) {
      final Optional<TituloLancamento> tituloLancamento =
          tituloLancamentoGateway.buscarPorNomeUsuarioEmail(
              obj.getNome().getNome(), obj.getUsuario().getEmail());
      if (!tituloLancamento.isPresent()) {
        if (obj.getTipo().equals(Tipo.RECEITA)) {
          obj.getNome().setAplicarNaReceita(true);
          obj.getNome().setAplicarNaDespesa(false);
        } else if (obj.getTipo().equals(Tipo.DESPESA)) {
          obj.getNome().setAplicarNaReceita(false);
          obj.getNome().setAplicarNaDespesa(true);
        }
        obj.getNome().setDataInclusao(LocalDateTime.now());
        obj.getNome().setDiaVencimento(obj.getVencimento().getDayOfMonth());
        tituloLancamentoGateway.salvar(obj.getNome());
      } else {
        if (obj.getTipo().equals(Tipo.RECEITA)) {
          tituloLancamento.get().setAplicarNaReceita(true);
          tituloLancamento.get().setAplicarNaDespesa(false);
        } else if (obj.getTipo().equals(Tipo.DESPESA)) {
          tituloLancamento.get().setAplicarNaReceita(false);
          tituloLancamento.get().setAplicarNaDespesa(true);
        }
        tituloLancamento.get().setDiaVencimento(obj.getVencimento().getDayOfMonth());
        tituloLancamentoGateway.atualizar(tituloLancamento.get());
      }
    }

    if (obj.getCentroCustoPrimario() != null
        && !centroCustoGateway
            .buscarPorNomeUsuarioEmail(
                obj.getCentroCustoPrimario().getNome(), obj.getUsuario().getEmail())
            .isPresent()) {
      if (obj.getTipo().equals(Tipo.RECEITA)) {
        obj.getCentroCustoPrimario().setAplicarNaReceita(true);
        obj.getCentroCustoPrimario().setAplicarNaDespesa(false);
      } else if (obj.getTipo().equals(Tipo.DESPESA)) {
        obj.getCentroCustoPrimario().setAplicarNaReceita(false);
        obj.getCentroCustoPrimario().setAplicarNaDespesa(true);
      }
      obj.getCentroCustoPrimario().setDataInclusao(LocalDateTime.now());
      centroCustoGateway.salvar(obj.getCentroCustoPrimario());
    }

    if (obj.getCentroCustoSecundario() != null
        && !centroCustoGateway
            .buscarPorNomeUsuarioEmail(
                obj.getCentroCustoSecundario().getNome(), obj.getUsuario().getEmail())
            .isPresent()) {
      if (obj.getTipo().equals(Tipo.RECEITA)) {
        obj.getCentroCustoSecundario().setAplicarNaReceita(true);
        obj.getCentroCustoSecundario().setAplicarNaDespesa(false);
      } else if (obj.getTipo().equals(Tipo.DESPESA)) {
        obj.getCentroCustoSecundario().setAplicarNaReceita(false);
        obj.getCentroCustoSecundario().setAplicarNaDespesa(true);
      }
      obj.getCentroCustoSecundario().setDataInclusao(LocalDateTime.now());
      centroCustoGateway.salvar(obj.getCentroCustoSecundario());
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
    } else {
      ContaBancaria contaBancariaDefault =
          contaBancariaGateway.buscarDefault(obj.getUsuario().getEmail());
      if (contaBancariaDefault != null) {
        obj.setContaBancaria(contaBancariaDefault);
      }
    }

    final int qtdParcelas = obj.getParcela();
    obj.setQuantidadeTotalParcelas(qtdParcelas);
    LocalDate vencimento = obj.getVencimento();
    int dayOfMonth = vencimento.getDayOfMonth();
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
        if (dataValida(dayOfMonth, vencimento.plusMonths(1))) {
          LocalDate validDate =
              DateUtils.getValidDate(
                  dayOfMonth,
                  vencimento.plusMonths(1).getMonthValue(),
                  vencimento.plusMonths(1).getYear());

          obj.setVencimento(validDate);
          vencimento = validDate;
        } else {
          obj.setVencimento(vencimento.plusMonths(1));
          vencimento = vencimento.plusMonths(1);
        }
      }
      obj.setObservacao(obj.getObservacao().toUpperCase());

      repository.save(obj);
      if (objRet == null) {
        objRet = obj;
      }
    }

    return objRet;
  }

  public Lancamento alterar(final Lancamento obj) {
    obj.setDataAlteracao(LocalDateTime.now());
    if (obj.getContaBancaria() == null) {
      ContaBancaria contaBancariaDefault =
          contaBancariaGateway.buscarDefault(obj.getUsuario().getEmail());
      if (contaBancariaDefault != null) {
        obj.setContaBancaria(contaBancariaDefault);
      }
    }
    return repository.save(obj);
  }

  public void alterarTipo(final String id) {
    Lancamento obj = this.buscarPorCodigo(id);
    obj.setTipo(obj.getTipo().equals(Tipo.RECEITA) ? Tipo.DESPESA : Tipo.RECEITA);
    obj.setDataAlteracao(LocalDateTime.now());
    repository.save(obj);
  }

  public void baixar(final String id, final Baixa baixa) {
    Lancamento obj = this.buscarPorCodigo(id);
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

    if (baixa.getContaBancaria() != null) {
      contaBancariaGateway
          .buscarPorNomeUsuarioEmail(
              baixa.getContaBancaria().getNome(), baixa.getUsuario().getEmail())
          .ifPresent(
              contaBancaria -> {
                if (contaBancaria.getAtualizarSaldoBancarioNaBaixaTitulo()) {
                  contaBancariaGateway.atualizarSaldo(
                      contaBancaria.getId(),
                      contaBancaria.getUsuario().getEmail(),
                      obj.getValorParcela(),
                      Operacao.DEBITO);
                }
              });
    }
  }

  public void estornar(final String id, final Baixa baixa) {
    Lancamento obj = this.buscarPorCodigo(id);
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

    if (baixa.getContaBancaria() != null) {
      contaBancariaGateway
          .buscarPorNomeUsuarioEmail(
              baixa.getContaBancaria().getNome(), baixa.getUsuario().getEmail())
          .ifPresent(
              contaBancaria -> {
                if (contaBancaria.getAtualizarSaldoBancarioNaBaixaTitulo()) {
                  contaBancariaGateway.atualizarSaldo(
                      contaBancaria.getId(),
                      contaBancaria.getUsuario().getEmail(),
                      obj.getValorParcela(),
                      Operacao.CREDITO);
                }
              });
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

  public void upload(final String email, final String str) {
    Usuario usuario = usuarioGateway.buscarPorEmail(email, true);

    if (usuario != null) {
      LancamentoUpload obj = new LancamentoUpload();
      String[] strArray = str.split(";");

      obj.setTipo(TipoUpload.toEnum(strArray[0]));
      obj.setDescricao(strArray[1].toUpperCase());
      obj.setCentroCustoPrimario(strArray[2].toUpperCase());
      obj.setCentroCustoSecundario(strArray[3].toUpperCase());
      obj.setVencimento(getVencimento(strArray[4]));
      obj.setValorParcela(BigDecimal.valueOf(Double.valueOf(strArray[5].replace(",", "."))));
      obj.setParcela(Integer.valueOf(strArray[6]));
      obj.setGerarParcelaUnica(Boolean.valueOf(strArray[7]));
      obj.setContaBancaria(strArray[8].toUpperCase());
      obj.setObservacao(strArray[9].toUpperCase());
      obj.setStatus(strArray[10].toUpperCase());
      obj.setTipoLancamento(strArray[11].toUpperCase());
      obj.setId(null);
      obj.setDataInclusao(LocalDateTime.now());
      obj.setUsuario(usuario);
      obj.setProcessado(false);

      uploadRepository.save(obj);
    }
  }

  private LocalDate getVencimento(final String dateString) {
    LocalDate date;
    try {
      date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(PATTERN_DATE_SHORT));
    } catch (Exception e) {
      date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(PATTERN_DATE_LONG));
    }

    return date;
  }

  private boolean dataValida(final int dayOfMonth, final LocalDate date) {
    String data =
        date.getYear()
            + "-"
            + StringUtils.leftPad(String.valueOf(date.getMonthValue()), 2, "0")
            + "-"
            + StringUtils.leftPad(String.valueOf(dayOfMonth), 2, "0");
    return DateUtils.isValid(data);
  }
}
