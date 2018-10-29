package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.events.ResourceEvent;
import com.vanderlei.cfp.gateways.UsuarioGateway;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.UsuarioConverter;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.USUARIO)
public class UsuarioController {
  static final String TAG_CONTROLLER = "usuario-controller";

  @Autowired private UsuarioGateway gateway;

  @Autowired private UsuarioDataContractConverter dataContractConverter;

  @Autowired private UsuarioConverter converter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Busca usuário por código",
      response = UsuarioDataContract.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Usuário encontrado",
            response = UsuarioDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Usuário não encontrado")
      })
  @RequestMapping(
      value = "/id/{id}",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Object> buscaPorId(
      @ApiParam(value = "Identificador do usuário", required = true) @PathVariable("id")
          final String id) {
    Usuario obj = gateway.buscarPorCodigo(id);
    final UsuarioDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca usuário por email",
      response = UsuarioDataContract.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Usuário encontrado",
            response = UsuarioDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Usuário não encontrado")
      })
  @RequestMapping(
      value = "/email",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Object> buscaPorEmail(
      @ApiParam(value = "Email do usuário", required = true) @RequestParam(value = "value")
          final String email) {
    Usuario obj = gateway.buscarPorEmail(email, true);
    final UsuarioDataContract dataContract = dataContractConverter.convert(obj);
    return ResponseEntity.ok().body(dataContract);
  }

  @ApiOperation(
      value = "Cadastrar novo usuário",
      response = Usuario.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Cadastrado com sucesso!", response = Usuario.class),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE},
      method = POST)
  ResponseEntity<Usuario> inserir(
      @ApiParam(value = "Usuário") @Valid @RequestBody final UsuarioDataContract dataContract,
      HttpServletResponse response) {
    Usuario obj = converter.convert(dataContract);
    gateway.inserir(obj);
    publisher.publishEvent(new ResourceEvent(this, response, obj.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(obj);
  }

  @ApiOperation(
      value = "Atualizar usuário",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Atualizado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/{email}", method = PUT)
  ResponseEntity<Void> atualizar(
      @ApiParam(value = "Usuário") @Valid @RequestBody final UsuarioDataContract dataContract,
      @ApiParam(value = "E-mail do usuário") @PathVariable("email") final String email) {
    Usuario obj = gateway.buscarPorEmail(email, true);
    Parsers.parse(obj.getId(), obj, dataContract);
    gateway.atualizar(obj);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @ApiOperation(
      value = "Ativar usuário",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Ativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/ativar/{email}", method = PUT)
  ResponseEntity<Void> ativar(
      @ApiParam(value = "E-mail do usuário") @PathVariable("email") final String email) {
    gateway.ativar(email);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  @ApiOperation(
      value = "Desativar usuário",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Desativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/desativar/{email}", method = PUT)
  ResponseEntity<Void> desativar(
      @ApiParam(value = "E-mail do usuário") @PathVariable("email") final String email) {
    gateway.desativar(email);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Atualizar foto pessoal do usuário",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Atualizado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/foto", method = POST)
  ResponseEntity<Void> uploadProfilePicture(
      @ApiParam(value = "Arquivo de Foto") @RequestParam(name = "arquivo")
          MultipartFile multipartFile) {
    URI uri = gateway.atualizarFotoPessoal(multipartFile);
    return ResponseEntity.created(uri).build();
  }
}
