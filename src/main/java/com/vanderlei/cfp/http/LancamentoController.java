package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.Baixa;
import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.LancamentoGateway;
import com.vanderlei.cfp.gateways.converters.*;
import com.vanderlei.cfp.http.data.*;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.LANCAMENTO)
public class LancamentoController {

  static final String TAG_CONTROLLER = "lancamento-controller";

  @Autowired private LancamentoGateway gateway;

  @Autowired private LancamentoDataContractConverter dataContractConverter;

  @Autowired private LancamentoFiltroDataContractConverter filtroDataContractConverter;

  @Autowired private LancamentoConverter converter;

  @Autowired private LancamentoEstatisticaDataContractConverter estatisticaConverter;

  @Autowired private TituloLancamentoDataContractConverter tituloLancamentoDataContractConverter;

  @Autowired private CentroCustoDataContractConverter centroCustoDataContractConverter;

  @Autowired private ContaBancariaDataContractConverter contaBancariaDataContractConverter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private BaixaConverter baixaConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Busca lançamento por id e email do usuário",
      response = LancamentoDataContract.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Lançamento encontrado",
            response = LancamentoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/email/id",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<LancamentoDataContract> buscaLancamentoPorIdUsuario(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Identificador do lancamento", required = true) @RequestParam(value = "id")
          final String id) {
    LancamentoDataContract obj =
        dataContractConverter.convert(gateway.buscarPorCodigoUsuarioEmail(id, email));
    return ResponseEntity.ok().body(obj);
  }

  @ApiOperation(
      value = "Busca todos os lançamentos de crédito por usuário (paginado)",
      response = LancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/credito",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<LancamentoDataContract>> buscaTodosCreditoPorUsuarioPaginado(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "500")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "vencimento")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<LancamentoDataContract> objList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(
                email, Tipo.RECEITA, page, linesPerPage, orderBy, direction));
    return objList.getTotalElements() > 0
        ? ResponseEntity.ok().body(objList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os lançamentos de crédito por usuário e período (paginado)",
      response = LancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/credito/periodo",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<LancamentoDataContract>> buscaTodosCreditoPeriodoPorUsuarioPaginado(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Data inicial")
          @RequestParam(value = "from")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          final LocalDate from,
      @ApiParam(value = "Data final")
          @RequestParam(value = "to")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          final LocalDate to,
      @ApiParam(value = "Descrição")
          @RequestParam(value = "description", defaultValue = StringUtils.EMPTY)
          final String description,
      @ApiParam(value = "Somente títulos em aberto")
          @RequestParam(value = "onlyOpen", defaultValue = "Sim")
          final String onlyOpen,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "500")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "vencimento")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction,
      @ApiParam(value = "Ordenação Secundária")
          @RequestParam(value = "orderByTwo", defaultValue = "nome.nome")
          final String orderByTwo,
      @ApiParam(value = "Direção Secundária")
          @RequestParam(value = "directionTwo", defaultValue = "ASC")
          final String directionTwo) {
    Page<LancamentoDataContract> objList =
        dataContractConverter.convert(
            gateway.buscarTodosPorPeriodoUsuarioPaginado(
                email,
                from,
                to,
                description,
                getStatus(onlyOpen.equalsIgnoreCase("Sim")),
                Tipo.RECEITA,
                page,
                linesPerPage,
                orderBy,
                direction,
                orderByTwo,
                directionTwo));
    return objList.getTotalElements() > 0
        ? ResponseEntity.ok().body(objList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os lançamentos de débito por usuário (paginado)",
      response = LancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/debito",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<LancamentoDataContract>> buscaTodosDebitoPorUsuarioPaginado(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "500")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "vencimento")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<LancamentoDataContract> objList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(
                email, Tipo.DESPESA, page, linesPerPage, orderBy, direction));
    return objList.getTotalElements() > 0
        ? ResponseEntity.ok().body(objList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os lançamentos de débito por usuário e período (paginado)",
      response = LancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/debito/periodo",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<LancamentoDataContract>> buscaTodosDebitoPeriodoPorUsuarioPaginado(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Data inicial")
          @RequestParam(value = "from")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          final LocalDate from,
      @ApiParam(value = "Data final")
          @RequestParam(value = "to")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          final LocalDate to,
      @ApiParam(value = "Descrição")
          @RequestParam(value = "description", defaultValue = StringUtils.EMPTY)
          final String description,
      @ApiParam(value = "Somente títulos em aberto")
          @RequestParam(value = "onlyOpen", defaultValue = "Sim")
          final String onlyOpen,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "500")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "vencimento")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction,
      @ApiParam(value = "Ordenação Secundária")
          @RequestParam(value = "orderByTwo", defaultValue = "nome.nome")
          final String orderByTwo,
      @ApiParam(value = "Direção Secundária")
          @RequestParam(value = "directionTwo", defaultValue = "ASC")
          final String directionTwo) {
    Page<LancamentoDataContract> objList =
        dataContractConverter.convert(
            gateway.buscarTodosPorPeriodoUsuarioPaginado(
                email,
                from,
                to,
                description,
                getStatus(onlyOpen.equalsIgnoreCase("Sim")),
                Tipo.DESPESA,
                page,
                linesPerPage,
                orderBy,
                direction,
                orderByTwo,
                directionTwo));
    return objList.getTotalElements() > 0
        ? ResponseEntity.ok().body(objList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca parcelas de lançamento por id e usuário (paginado)",
      response = LancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/parcelas/{id}",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<LancamentoDataContract>> buscaParcelasPaginado(
      @ApiParam(value = "Identificador do lançamento") @PathVariable final String id,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "500")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "vencimento")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<LancamentoDataContract> objList =
        dataContractConverter.convert(
            gateway.buscarParcelasLancamentoAbertoPaginado(
                id, page, linesPerPage, orderBy, direction));
    return objList.getTotalElements() > 0
        ? ResponseEntity.ok().body(objList)
        : ResponseEntity.ok().body(new PageImpl<LancamentoDataContract>(new ArrayList<>()));
  }

  @ApiOperation(
      value = "Busca lançamentos de crédito por período para gráfico sintetico",
      response = LancamentoEstatisticaCentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Lançamentos encontrados",
            response = LancamentoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamentos não encontrados")
      })
  @RequestMapping(
      value = "/estatisticas/credito/por-centrocusto-sintetico",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<List<LancamentoEstatisticaCentroCustoDataContract>>
      buscaLancamentoCreditoEstatisticaCentroCustoSintetico(
          @ApiParam(value = "Identificador do usuário", required = true)
              @RequestParam(value = "email")
              final String email,
          @ApiParam(value = "Data inicial")
              @RequestParam(value = "from")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate from,
          @ApiParam(value = "Data final")
              @RequestParam(value = "to")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate to,
          @ApiParam(value = "Centro de Custo") @RequestParam(value = "costCenter")
              final String costCenter) {
    List<LancamentoDataContract> objLancamentoList =
        dataContractConverter.convert(
            gateway.buscarEstatisticaCentroCusto(Tipo.RECEITA, email, from, to, costCenter));

    return ResponseEntity.ok()
        .body(
            objLancamentoList.stream()
                .collect(
                    Collectors.groupingBy(
                        a -> a.getCentroCustoPrimario().getNome(),
                        Collectors.mapping(
                            LancamentoDataContract::getValorParcela,
                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet()
                .stream()
                .collect(Collectors.toList())
                .stream()
                .map(lancamento -> estatisticaConverter.convert(lancamento))
                .filter(
                    lancamento -> lancamento.getCentroCusto().getUsuario().getEmail().equals(email))
                .collect(Collectors.toList()));
  }

  @ApiOperation(
      value = "Busca lançamentos de débito por período para gráfico sintetico",
      response = LancamentoEstatisticaCentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Lançamentos encontrados",
            response = LancamentoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamentos não encontrados")
      })
  @RequestMapping(
      value = "/estatisticas/debito/por-centrocusto-sintetico",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<List<LancamentoEstatisticaCentroCustoDataContract>>
      buscaLancamentoDebitoEstatisticaCentroCustoSintetico(
          @ApiParam(value = "Identificador do usuário", required = true)
              @RequestParam(value = "email")
              final String email,
          @ApiParam(value = "Data inicial")
              @RequestParam(value = "from")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate from,
          @ApiParam(value = "Data final")
              @RequestParam(value = "to")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate to,
          @ApiParam(value = "Centro de Custo") @RequestParam(value = "costCenter")
              final String costCenter) {
    List<LancamentoDataContract> objLancamentoList =
        dataContractConverter.convert(
            gateway.buscarEstatisticaCentroCusto(Tipo.DESPESA, email, from, to, costCenter));

    return ResponseEntity.ok()
        .body(
            objLancamentoList.stream()
                .collect(
                    Collectors.groupingBy(
                        a -> a.getCentroCustoPrimario().getNome(),
                        Collectors.mapping(
                            LancamentoDataContract::getValorParcela,
                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet()
                .stream()
                .collect(Collectors.toList())
                .stream()
                .map(lancamento -> estatisticaConverter.convert(lancamento))
                .filter(
                    lancamento -> lancamento.getCentroCusto().getUsuario().getEmail().equals(email))
                .collect(Collectors.toList()));
  }

  @ApiOperation(
      value = "Busca lançamentos de crédito por período para gráfico analitico",
      response = LancamentoEstatisticaCentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Lançamentos encontrados",
            response = LancamentoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamentos não encontrados")
      })
  @RequestMapping(
      value = "/estatisticas/credito/por-centrocusto-analitico",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<List<LancamentoEstatisticaCentroCustoDataContract>>
      buscaLancamentoCreditoEstatisticaCentroCustoAnalitico(
          @ApiParam(value = "Identificador do usuário", required = true)
              @RequestParam(value = "email")
              final String email,
          @ApiParam(value = "Data inicial")
              @RequestParam(value = "from")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate from,
          @ApiParam(value = "Data final")
              @RequestParam(value = "to")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate to,
          @ApiParam(value = "Centro de Custo") @RequestParam(value = "costCenter")
              final String costCenter) {
    List<LancamentoDataContract> objLancamentoList =
        dataContractConverter.convert(
            gateway.buscarEstatisticaCentroCusto(Tipo.RECEITA, email, from, to, costCenter));

    return ResponseEntity.ok()
        .body(
            objLancamentoList.stream()
                .collect(
                    Collectors.groupingBy(
                        a -> a.getCentroCustoSecundario().getNome(),
                        Collectors.mapping(
                            LancamentoDataContract::getValorParcela,
                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet()
                .stream()
                .collect(Collectors.toList())
                .stream()
                .map(lancamento -> estatisticaConverter.convert(lancamento))
                .filter(
                    lancamento -> lancamento.getCentroCusto().getUsuario().getEmail().equals(email))
                .collect(Collectors.toList()));
  }

  @ApiOperation(
      value = "Busca lançamentos de débito por período para gráfico analitico",
      response = LancamentoEstatisticaCentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Lançamentos encontrados",
            response = LancamentoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamentos não encontrados")
      })
  @RequestMapping(
      value = "/estatisticas/debito/por-centrocusto-analitico",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<List<LancamentoEstatisticaCentroCustoDataContract>>
      buscaLancamentoDebitoEstatisticaCentroCustoAnalitico(
          @ApiParam(value = "Identificador do usuário", required = true)
              @RequestParam(value = "email")
              final String email,
          @ApiParam(value = "Data inicial")
              @RequestParam(value = "from")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate from,
          @ApiParam(value = "Data final")
              @RequestParam(value = "to")
              @DateTimeFormat(pattern = "yyyy-MM-dd")
              final LocalDate to,
          @ApiParam(value = "Centro de Custo") @RequestParam(value = "costCenter")
              final String costCenter) {
    List<LancamentoDataContract> objLancamentoList =
        dataContractConverter.convert(
            gateway.buscarEstatisticaCentroCusto(Tipo.DESPESA, email, from, to, costCenter));

    return ResponseEntity.ok()
        .body(
            objLancamentoList.stream()
                .collect(
                    Collectors.groupingBy(
                        a -> a.getCentroCustoSecundario().getNome(),
                        Collectors.mapping(
                            LancamentoDataContract::getValorParcela,
                            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))))
                .entrySet()
                .stream()
                .collect(Collectors.toList())
                .stream()
                .map(lancamento -> estatisticaConverter.convert(lancamento))
                .filter(
                    lancamento -> lancamento.getCentroCusto().getUsuario().getEmail().equals(email))
                .collect(Collectors.toList()));
  }

  @ApiOperation(
      value = "Busca todos os lançamentos ativos por usuário e tipo",
      response = LancamentoDataContract.class,
      responseContainer = "Page",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/ativos/tipo",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<List<LancamentoFiltroDataContract>> buscaTodosAtivosPorTipo(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Tipo", required = true) @RequestParam(value = "tipo") final String tipo) {
    List<LancamentoFiltroDataContract> dataContractList =
        filtroDataContractConverter.convert(gateway.buscarTodosAtivosPorUsuarioTipo(email, tipo));
    return dataContractList.size() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os lançamentos ativos por usuário",
      response = LancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais lançamentos encontrados",
            response = LancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Lançamento não encontrado")
      })
  @RequestMapping(
      value = "/ativos",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<List<LancamentoFiltroDataContract>> buscaTodosAtivos(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email) {
    List<LancamentoFiltroDataContract> dataContractList =
        filtroDataContractConverter.convert(gateway.buscarTodosAtivosPorUsuario(email));
    return dataContractList.size() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Cadastrar novo lançamento",
      response = Lancamento.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Cadastrado com sucesso!", response = Lancamento.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<Lancamento> inserir(
      @ApiParam(value = "Lançamento") @Valid @RequestBody final LancamentoDataContract dataContract,
      HttpServletResponse response) {
    Lancamento obj = converter.convert(dataContract);
    obj = gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(
      value = "Alterar lançamento",
      response = Lancamento.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Alterado com sucesso!", response = Lancamento.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = PUT)
  ResponseEntity<LancamentoDataContract> alterar(
      @ApiParam(value = "Alterar todos os lançamentos em aberto", required = true)
          @RequestParam(value = "updateAll", defaultValue = "False")
          final String updateAll,
      @ApiParam(value = "Lançamento") @Valid @RequestBody final LancamentoDataContract dataContract,
      HttpServletResponse response) {

    Lancamento obj = converter.convert(dataContract);
    if (!Boolean.valueOf(updateAll)) {
      obj = gateway.alterar(dataContract.getId(), obj);
    } else {
      // Continuar com logica de alteração de todos os registros com parcelas em aberto
    }
    LancamentoDataContract objReturn = dataContractConverter.convert(obj);

    publisher.publishEvent(new ResourceEvent(this, response, objReturn.getId()));
    return ResponseEntity.status(HttpStatus.OK).body(objReturn);
  }

  @ApiOperation(
      value = "Alterar o tipo do lançamento",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Alterado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/tipo/{id}", method = PUT)
  ResponseEntity<Void> mudarTipo(
      @ApiParam(value = "Identificador do lançamento") @PathVariable final String id) {
    gateway.alterarTipo(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Efetuar a baixa do lançamento",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Confirmado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/baixar/{id}", method = PUT)
  ResponseEntity<Void> baixar(
      @ApiParam(value = "Identificador do lançamento") @PathVariable final String id,
      @ApiParam(value = "Informações da baixa do lançamento") @Valid @RequestBody
          final BaixaDataContract dataContract) {
    Baixa baixa = baixaConverter.convert(dataContract);
    gateway.baixar(id, baixa);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Efetuar o estorno do lançamento",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Confirmado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/estornar/{id}", method = PUT)
  ResponseEntity<Void> estornar(
      @ApiParam(value = "Identificador do lançamento") @PathVariable final String id,
      @ApiParam(value = "Informações da baixa do lançamento") @Valid @RequestBody
          final BaixaDataContract dataContract) {
    Baixa baixa = baixaConverter.convert(dataContract);
    gateway.estornar(id, baixa);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Ativar lançamento",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Ativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/ativar/{id}", method = PUT)
  ResponseEntity<Void> ativar(
      @ApiParam(value = "Identificador do lançamento") @PathVariable final String id) {
    gateway.ativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Desativar lançamento",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Desativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/desativar/{id}", method = PUT)
  ResponseEntity<Void> desativar(
      @ApiParam(value = "Identificador do lançamento") @PathVariable final String id) {
    gateway.desativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Upload de novos lançamentos",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Upload efetuado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      value = "/upload",
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<Void> upload(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Tipo", required = true) @RequestParam(value = "tipo") final String tipo,
      @ApiParam(value = "Lançamento") @RequestBody
          final LancamentoUploadDataContract dataContract) {

    gateway.upload(email, dataContract);
    return ResponseEntity.noContent().build();
  }

  @RequestMapping(value = "/relatorio/lancamento", method = GET)
  ResponseEntity<byte[]> relatorio(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Tipo do lançamento", required = true) @RequestParam(value = "type")
          final String type,
      @ApiParam(value = "Data inicial")
          @RequestParam(value = "from")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          final LocalDate from,
      @ApiParam(value = "Data final")
          @RequestParam(value = "to")
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          final LocalDate to,
      @ApiParam(value = "Descrição")
          @RequestParam(value = "description", defaultValue = StringUtils.EMPTY)
          final String description,
      @ApiParam(value = "Somente títulos em aberto")
          @RequestParam(value = "onlyOpen", defaultValue = "Sim")
          final String onlyOpen,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "500")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "vencimento")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction,
      @ApiParam(value = "Ordenação Secundária")
          @RequestParam(value = "orderByTwo", defaultValue = "nome.nome")
          final String orderByTwo,
      @ApiParam(value = "Direção Secundária")
          @RequestParam(value = "directionTwo", defaultValue = "ASC")
          final String directionTwo)
      throws Exception {
    byte[] report =
        gateway.report(
            email,
            from,
            to,
            description,
            getStatus(onlyOpen.equalsIgnoreCase("Sim")),
            getType(type),
            page,
            linesPerPage,
            orderBy,
            direction,
            orderByTwo,
            directionTwo);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, APPLICATION_PDF_VALUE).body(report);
  }

  private Tipo getType(final String type) {
    return type.equalsIgnoreCase("Receita") ? Tipo.RECEITA : Tipo.DESPESA;
  }

  private List<Status> getStatus(final boolean onlyOpen) {
    return onlyOpen
        ? Arrays.asList(Status.ABERTO)
        : Arrays.asList(Status.ABERTO, Status.RECEBIDO, Status.PAGO);
  }
}
