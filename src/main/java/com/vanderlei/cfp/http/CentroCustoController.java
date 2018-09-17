package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.CentroCusto;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.CENTRO_CUSTO)
public class CentroCustoController {

  @Autowired private CentroCustoGateway gateway;

  @Autowired private CentroCustoDataContractConverter dataContractConverter;

  @Autowired private CentroCustoConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @ApiOperation(value = "Buscar por codigo")
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

  @ApiOperation(value = "Buscar todos por usuário")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<CentroCustoDataContract>> buscaTodos() {
    Collection<CentroCusto> objList = gateway.buscarTodosPorUsuario();
    Collection<CentroCustoDataContract> dataContractList =
        objList
            .stream()
            .map(
                obj ->
                    new CentroCustoDataContract(
                        obj.getId(),
                        obj.getNome(),
                        obj.getAplicarNaDespesa(),
                        obj.getAplicarNaReceita(),
                        usuarioDataContractConverter.convert(obj.getUsuario())))
            .collect(Collectors.toList());
    return ResponseEntity.ok().body(dataContractList);
  }

  @ApiOperation(value = "Buscar todos ativos por usuário")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(
      value = "/ativos",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<CentroCustoDataContract>> buscaTodosAtivos() {
    Collection<CentroCusto> objList = gateway.buscarTodosAtivosPorUsuario();
    Collection<CentroCustoDataContract> dataContractList =
        objList
            .stream()
            .map(
                obj ->
                    new CentroCustoDataContract(
                        obj.getId(),
                        obj.getNome(),
                        obj.getAplicarNaDespesa(),
                        obj.getAplicarNaReceita(),
                        usuarioDataContractConverter.convert(obj.getUsuario())))
            .collect(Collectors.toList());
    return ResponseEntity.ok().body(dataContractList);
  }

  @ApiOperation(value = "Criar novo")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Inserido com sucesso")})
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> inserir(
      @Valid @RequestBody final CentroCustoDataContract dataContract) {
    CentroCusto obj = converter.convert(dataContract);
    gateway.inserir(obj);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(obj.getId())
            .toUri();
    return ResponseEntity.created(uri).build();
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
