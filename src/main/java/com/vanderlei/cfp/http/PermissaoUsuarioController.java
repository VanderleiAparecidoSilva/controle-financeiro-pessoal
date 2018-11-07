package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.entities.PermissaoUsuario;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.PermissaoUsuarioGateway;
import com.vanderlei.cfp.gateways.converters.PermissaoUsuarioConverter;
import com.vanderlei.cfp.http.data.PermissaoUsuarioDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.PERMISSAO_USUARIO)
public class PermissaoUsuarioController {
  static final String TAG_CONTROLLER = "permissao-usuario-controller";

  @Autowired private PermissaoUsuarioGateway gateway;

  @Autowired private PermissaoUsuarioConverter converter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Cadastrar nova permissão do usuário",
      response = PermissaoUsuario.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Cadastrado com sucesso!", response = Permissao.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<PermissaoUsuario> inserir(
      @ApiParam(value = "Permissão do Usuário") @Valid @RequestBody
          final PermissaoUsuarioDataContract dataContract,
      HttpServletResponse response) {
    PermissaoUsuario obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(
      value = "Ativar permissão do usuário",
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
      @ApiParam(value = "Identificador da permissão do usuário") @PathVariable("id")
          final String id) {
    gateway.ativar(id);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Desativar permissão do usuário",
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
      @ApiParam(value = "Identificador da permissão do usuário") @PathVariable("id")
          final String id) {
    gateway.desativar(id);
    return ResponseEntity.noContent().build();
  }
}
