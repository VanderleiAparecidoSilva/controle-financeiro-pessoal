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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.CENTRO_CUSTO)
public class CentroCustoController {

  @Autowired private CentroCustoGateway gateway;

  @Autowired private CentroCustoDataContractConverter dataContractConverter;

  @Autowired private CentroCustoConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(value = "Buscar por codigo e usuário")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> buscaPorId(@PathVariable final String id) {
    CentroCusto obj = gateway.buscarPorCodigo(id);
    final CentroCustoDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(value = "Buscar todos paginados por usuário")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<CentroCustoDataContract>> buscaTodosPorPagina(
      @RequestParam(value = "page", defaultValue = "0") final Integer page,
      @RequestParam(value = "linesPerPage", defaultValue = "24") final Integer linesPerPage,
      @RequestParam(value = "orderBy", defaultValue = "instante") final String orderBy,
      @RequestParam(value = "direction", defaultValue = "DESC") final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return ResponseEntity.ok().body(dataContractList);
  }

  @ApiOperation(value = "Buscar todos ativos paginados por usuário")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(
      value = "/ativos",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<CentroCustoDataContract>> buscaTodosAtivosPorPagina(
      @RequestParam(value = "page", defaultValue = "0") final Integer page,
      @RequestParam(value = "linesPerPage", defaultValue = "24") final Integer linesPerPage,
      @RequestParam(value = "orderBy", defaultValue = "instante") final String orderBy,
      @RequestParam(value = "direction", defaultValue = "DESC") final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosAtivosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return ResponseEntity.ok().body(dataContractList);
  }

  @ApiOperation(value = "Criar novo")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Inserido com sucesso")})
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CentroCusto> inserir(
      @Valid @RequestBody final CentroCustoDataContract dataContract,
      HttpServletResponse response) {
    CentroCusto obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(value = "Atualizar")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Atualizado com sucesso")})
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> atualizar(
      @Valid @RequestBody final CentroCustoDataContract dataContract,
      @PathVariable final String id) {
    CentroCusto obj = gateway.buscarPorCodigo(id);
    Parsers.parse(id, obj, dataContract);
    gateway.atualizar(obj);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(value = "Ativar")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Ativado com sucesso")})
  @RequestMapping(value = "/ativar/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> ativar(@PathVariable final String id) {
    gateway.ativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(value = "Desativar")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Desativado com sucesso")})
  @RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> desativar(@PathVariable final String id) {
    gateway.desativar(id);
    return ResponseEntity.noContent().build();
  }
}
