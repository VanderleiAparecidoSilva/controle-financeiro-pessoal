package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.CentroCustoGateway;
import com.vanderlei.cfp.gateways.converters.CentroCustoConverter;
import com.vanderlei.cfp.gateways.converters.CentroCustoDataContractConverter;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.CentroCustoDataContract;
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
@RequestMapping(UrlMapping.CENTRO_CUSTO)
public class CentroCustoController {

  @Autowired private CentroCustoGateway gateway;

  @Autowired private CentroCustoDataContractConverter dataContractConverter;

  @Autowired private CentroCustoConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Busca os centro de custos por código e usuário",
      response = CentroCustoDataContract.class,
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Centro de custos encontrado",
            response = CentroCustoDataContract.class,
            responseContainer = "List"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Centro de custo não encontrado")
      })
  @RequestMapping(
      value = "/{id}",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<CentroCustoDataContract> buscaPorId(
      @ApiParam(value = "Identificador do centro de custo", required = true) @PathVariable("id")
          final String id) {
    CentroCusto obj = gateway.buscarPorCodigo(id);
    final CentroCustoDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os centro de custos por usuário (paginado)",
      response = CentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais centro de custos encontrados",
            response = CentroCustoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Centro de custo não encontrado")
      })
  @RequestMapping(
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<CentroCustoDataContract>> buscaTodosPorPagina(
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "instante")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "DESC")
          final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return dataContractList.getSize() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os centro de custos ativados por usuário (paginado)",
      response = CentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Um ou mais centro de custos encontrados",
            response = CentroCustoDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Centro de custo não encontrado")
      })
  @RequestMapping(
      value = "/ativos",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<CentroCustoDataContract>> buscaTodosAtivosPorPagina(
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "instante")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "DESC")
          final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosAtivosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return dataContractList.getSize() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Cadastrar novo centro de custo",
      response = CentroCusto.class,
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Cadastrado com sucesso!", response = CentroCusto.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<CentroCusto> inserir(
      @ApiParam(value = "Centro de Custo") @Valid @RequestBody
          final CentroCustoDataContract dataContract,
      HttpServletResponse response) {
    CentroCusto obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(
      value = "Atualizar centro de custo",
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Atualizado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/{id}", method = PUT)
  ResponseEntity<Void> atualizar(
      @ApiParam(value = "Centro de Custo") @Valid @RequestBody
          final CentroCustoDataContract dataContract,
      @ApiParam(value = "Identificador do centro de custo") @PathVariable("id") final String id) {
    CentroCusto obj = gateway.buscarPorCodigo(id);
    Parsers.parse(id, obj, dataContract);
    gateway.atualizar(obj);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Ativar centro de custo",
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Ativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/ativar/{id}", method = PUT)
  ResponseEntity<Void> ativar(
      @ApiParam(value = "Identificador do centro de custo") @PathVariable("id") final String id) {
    gateway.ativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Desativar centro de custo",
      tags = {
        "centro-custo-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Desativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/desativar/{id}", method = PUT)
  ResponseEntity<Void> desativar(
      @ApiParam(value = "Identificador do centro de custo") @PathVariable("id") final String id) {
    gateway.desativar(id);
    return ResponseEntity.noContent().build();
  }
}
