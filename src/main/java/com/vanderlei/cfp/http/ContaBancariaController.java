package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.ContaBancariaGateway;
import com.vanderlei.cfp.gateways.converters.ContaBancariaConverter;
import com.vanderlei.cfp.gateways.converters.ContaBancariaDataContractConverter;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.ContaBancariaDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.CONTA_BANCARIA)
public class ContaBancariaController {

  @Autowired private ContaBancariaGateway gateway;

  @Autowired private ContaBancariaDataContractConverter dataContractConverter;

  @Autowired private ContaBancariaConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(value = "Buscar por codigo")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> buscaPorId(@PathVariable final String id) {
    ContaBancaria obj = gateway.buscarPorCodigo(id);
    final ContaBancariaDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(value = "Buscar todos por usuário")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Sucesso")})
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<ContaBancariaDataContract>> buscaTodos() {
    Collection<ContaBancaria> objList = gateway.buscarTodosPorUsuario();
    Collection<ContaBancariaDataContract> dataContractList =
        objList
            .stream()
            .map(
                obj ->
                    new ContaBancariaDataContract(
                        obj.getId(),
                        obj.getNome(),
                        obj.getNumeroContaBancaria(),
                        obj.getLimiteContaBancaria(),
                        obj.getSaldoContaBancaria(),
                        obj.getVincularSaldoBancarioNoTotalReceita(),
                        obj.getAtualizarSaldoBancarioNaBaixaTitulo(),
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
  public ResponseEntity<Collection<ContaBancariaDataContract>> buscaTodosAtivos() {
    Collection<ContaBancaria> objList = gateway.buscarTodosAtivosPorUsuario();
    Collection<ContaBancariaDataContract> dataContractList =
        objList
            .stream()
            .map(
                obj ->
                    new ContaBancariaDataContract(
                        obj.getId(),
                        obj.getNome(),
                        obj.getNumeroContaBancaria(),
                        obj.getLimiteContaBancaria(),
                        obj.getSaldoContaBancaria(),
                        obj.getVincularSaldoBancarioNoTotalReceita(),
                        obj.getAtualizarSaldoBancarioNaBaixaTitulo(),
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
  public ResponseEntity<ContaBancaria> inserir(
      @Valid @RequestBody final ContaBancariaDataContract dataContract,
      HttpServletResponse response) {
    ContaBancaria obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(value = "Atualizar")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Atualizado com sucesso")})
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> atualizar(
      @Valid @RequestBody final ContaBancariaDataContract dataContract,
      @PathVariable final String id) {
    ContaBancaria obj = gateway.buscarPorCodigo(id);
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

  @ApiOperation(value = "Atualizar Saldo")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "Saldo atualizado com sucesso")})
  @RequestMapping(value = "/saldo/{id}", method = RequestMethod.PUT)
  public ResponseEntity<Void> saldo(
      @PathVariable final String id,
      @PathVariable final Double valor,
      @PathVariable final Operacao operacao) {
    gateway.atualizarSaldo(id, valor, operacao);
    return ResponseEntity.noContent().build();
  }
}
