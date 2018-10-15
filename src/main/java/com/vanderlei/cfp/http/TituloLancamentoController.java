package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.TituloLancamento;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.TituloLancamentoGateway;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.TituloLancamentoConverter;
import com.vanderlei.cfp.gateways.converters.TituloLancamentoDataContractConverter;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.TituloLancamentoDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.TITULO_LANCAMENTO)
public class TituloLancamentoController {

  @Autowired private TituloLancamentoGateway gateway;

  @Autowired private TituloLancamentoDataContractConverter dataContractConverter;

  @Autowired private TituloLancamentoConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Busca título de lançamento por código e usuário",
      response = TituloLancamentoDataContract.class,
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Título de lançamento encontrado",
            response = TituloLancamentoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Título de lançamento não encontrado")
      })
  @RequestMapping(
      value = "/{id}",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Object> buscaPorId(
      @ApiParam(value = "Identificador do título de lançamento", required = true)
          @PathVariable("id")
          final String id) {
    TituloLancamento obj = gateway.buscarPorCodigo(id);
    final TituloLancamentoDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os título de lançamento por usuário (paginado)",
      response = TituloLancamentoDataContract.class,
      responseContainer = "List",
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais títulos de lançamento encontrados",
            response = TituloLancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Título de lançamento não encontrado")
      })
  @RequestMapping(
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<TituloLancamentoDataContract>> buscaTodosPorPagina(
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<TituloLancamentoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os título de lançamento ativos por usuário (paginado)",
      response = TituloLancamentoDataContract.class,
      responseContainer = "Page",
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais títulos de lançamento encontrados",
            response = TituloLancamentoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Título de lançamento não encontrado")
      })
  @RequestMapping(
      value = "/ativos",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<TituloLancamentoDataContract>> buscaTodosAtivosPorPagina(
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<TituloLancamentoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosAtivosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Cadastrar novo título de lançamento",
      response = TituloLancamento.class,
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 201,
            message = "Cadastrado com sucesso!",
            response = TituloLancamento.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<TituloLancamento> inserir(
      @ApiParam(value = "Título de lançamento") @Valid @RequestBody
          final TituloLancamentoDataContract dataContract,
      HttpServletResponse response) {
    TituloLancamento obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(
      value = "Atualizar título de lançamento",
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Atualizado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/{id}", method = PUT)
  ResponseEntity<Void> atualizar(
      @ApiParam(value = "Título de lançamento") @Valid @RequestBody
          final TituloLancamentoDataContract dataContract,
      @ApiParam(value = "Identificador do título de lançamento") @PathVariable("id")
          final String id) {
    TituloLancamento obj = gateway.buscarPorCodigo(id);
    Parsers.parse(id, obj, dataContract);
    gateway.atualizar(obj);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Ativar título de lançamento",
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Ativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/ativar/{id}", method = PUT)
  ResponseEntity<Void> ativar(
      @ApiParam(value = "Identificador do título de lançamento") @PathVariable("id")
          final String id) {
    gateway.ativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Desativar título de lançamento",
      tags = {
        "titulo-lancamento-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Desativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/desativar/{id}", method = PUT)
  ResponseEntity<Void> desativar(
      @ApiParam(value = "Identificador do título de lançamento") @PathVariable("id")
          final String id) {
    gateway.desativar(id);
    return ResponseEntity.noContent().build();
  }
}
