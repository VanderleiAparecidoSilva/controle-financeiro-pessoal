package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.ContaBancariaGateway;
import com.vanderlei.cfp.gateways.converters.ContaBancariaConverter;
import com.vanderlei.cfp.gateways.converters.ContaBancariaDataContractConverter;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.ContaBancariaDataContract;
import com.vanderlei.cfp.http.data.params.ContaBancariaSaldoDataContract;
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
@RequestMapping(UrlMapping.CONTA_BANCARIA)
public class ContaBancariaController {

  @Autowired private ContaBancariaGateway gateway;

  @Autowired private ContaBancariaDataContractConverter dataContractConverter;

  @Autowired private ContaBancariaConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Busca conta bancária por código e usuário",
      response = ContaBancariaDataContract.class,
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Conta bancária encontrada",
            response = ContaBancariaDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Conta bancária não encontrada")
      })
  @RequestMapping(
      value = "/{id}",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Object> buscaPorId(
      @ApiParam(value = "Identificador da conta bancária", required = true) @PathVariable("id")
          final String id) {
    ContaBancaria obj = gateway.buscarPorCodigo(id);
    final ContaBancariaDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todas as contas bancárias por usuário (paginado)",
      response = ContaBancariaDataContract.class,
      responseContainer = "List",
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Uma ou mais contas bancárias encontradas",
            response = ContaBancariaDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Conta bancária não encontrada")
      })
  @RequestMapping(
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<ContaBancariaDataContract>> buscaTodosPorPagina(
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<ContaBancariaDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todas as contas bancárias ativas por usuário (paginado)",
      response = ContaBancariaDataContract.class,
      responseContainer = "Page",
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Uma ou mais contas bancárias encontradas",
            response = ContaBancariaDataContract.class,
            responseContainer = "Page"),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Conta bancária não encontrada")
      })
  @RequestMapping(
      value = "/ativos",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<ContaBancariaDataContract>> buscaTodosAtivosPorPagina(
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<ContaBancariaDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosAtivosPorUsuarioPaginado(page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Cadastrar nova conta bancária",
      response = ContaBancaria.class,
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 201,
            message = "Cadastrada com sucesso!",
            response = ContaBancaria.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<ContaBancaria> inserir(
      @ApiParam(value = "Conta Bancária") @Valid @RequestBody
          final ContaBancariaDataContract dataContract,
      HttpServletResponse response) {
    ContaBancaria obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(
      value = "Atualizar conta bancária",
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Atualizada com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/{id}", method = PUT)
  ResponseEntity<Void> atualizar(
      @ApiParam(value = "Conta Bancária") @Valid @RequestBody
          final ContaBancariaDataContract dataContract,
      @ApiParam(value = "Identificador da conta bancária") @PathVariable("id") final String id) {
    ContaBancaria obj = gateway.buscarPorCodigo(id);
    Parsers.parse(id, obj, dataContract);
    gateway.atualizar(obj);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Ativar conta bancária",
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Ativada com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/ativar/{id}", method = PUT)
  ResponseEntity<Void> ativar(
      @ApiParam(value = "Identificador da conta bancária") @PathVariable("id") final String id) {
    gateway.ativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Desativar conta bancária",
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Desativada com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/desativar/{id}", method = PUT)
  ResponseEntity<Void> desativar(
      @ApiParam(value = "Identificador da conta bancária") @PathVariable("id") final String id) {
    gateway.desativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Atualizar saldo da conta bancária",
      tags = {
        "conta-bancaria-controller",
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Saldo bancário atualizado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/saldo/{id}", method = PUT)
  ResponseEntity<Void> saldo(
      @ApiParam(value = "Identificador da conta bancária") @PathVariable("id") final String id,
      @ApiParam(value = "Dados para atualização do saldo") @RequestBody
          final ContaBancariaSaldoDataContract saldo) {
    gateway.atualizarSaldo(id, saldo.getValor(), saldo.getOperacao());
    return ResponseEntity.noContent().build();
  }
}
