package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.enums.TipoUpload;
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
  static final String TAG_CONTROLLER = "centro-custo-controller";

  @Autowired private CentroCustoGateway gateway;

  @Autowired private CentroCustoDataContractConverter dataContractConverter;

  @Autowired private CentroCustoConverter converter;

  @Autowired private UsuarioDataContractConverter usuarioDataContractConverter;

  @Autowired private ApplicationEventPublisher publisher;

  @ApiOperation(
      value = "Busca centro de custo por código e usuário",
      response = CentroCustoDataContract.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Centro de custo encontrado",
            response = CentroCustoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Centro de custo não encontrado")
      })
  @RequestMapping(
      value = "/{email}/{id}",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<CentroCustoDataContract> buscaPorId(
      @ApiParam(value = "Identificador do usuário", required = true) @PathVariable("email")
          final String email,
      @ApiParam(value = "Identificador do centro de custo", required = true) @PathVariable("id")
          final String id) {
    CentroCusto obj = gateway.buscarPorCodigoUsuario(id, email);
    final CentroCustoDataContract dataContract = dataContractConverter.convert(obj);
    return obj != null ? ResponseEntity.ok().body(dataContract) : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os centro de custos por usuário (paginado)",
      response = CentroCustoDataContract.class,
      responseContainer = "List",
      tags = {
        TAG_CONTROLLER,
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
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosPorUsuarioPaginado(email, page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca todos os centro de custos ativos por usuário (paginado)",
      response = CentroCustoDataContract.class,
      responseContainer = "Page",
      tags = {
        TAG_CONTROLLER,
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
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarTodosAtivosPorUsuarioPaginado(
                email, page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Busca centro de custo por nome e usuário",
      response = CentroCustoDataContract.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Centro de custo encontrado",
            response = CentroCustoDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Centro de custo não encontrado")
      })
  @RequestMapping(
      value = "/email/nome",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<Page<CentroCustoDataContract>> buscaPorNome(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Nome do centro de custo", required = true) @RequestParam(value = "nome")
          final String nome,
      @ApiParam(value = "Ativo", required = true, defaultValue = "true") @RequestParam(value = "ativo")
        final Boolean ativo,
      @ApiParam(value = "Quantidade de páginas") @RequestParam(value = "page", defaultValue = "0")
          final Integer page,
      @ApiParam(value = "Quantidade de linhas por página")
          @RequestParam(value = "linesPerPage", defaultValue = "24")
          final Integer linesPerPage,
      @ApiParam(value = "Ordenação") @RequestParam(value = "orderBy", defaultValue = "nome")
          final String orderBy,
      @ApiParam(value = "Direção") @RequestParam(value = "direction", defaultValue = "ASC")
          final String direction) {
    Page<CentroCustoDataContract> dataContractList =
        dataContractConverter.convert(
            gateway.buscarPorNomeLikeUsuarioEmail(
                email, nome, ativo, page, linesPerPage, orderBy, direction));
    return dataContractList.getTotalElements() > 0
        ? ResponseEntity.ok().body(dataContractList)
        : ResponseEntity.notFound().build();
  }

  @ApiOperation(
      value = "Cadastrar novo centro de custo",
      response = CentroCusto.class,
      tags = {
        TAG_CONTROLLER,
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
      value = "Upload de novos centros de custo",
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
      @ApiParam(value = "Centro de Custo") @RequestBody final String dataContract) {

    if (dataContract.split(";")[0].equals(TipoUpload.CENTRO_CUSTO.getDescricao())) {
      gateway.upload(email, dataContract);
    }
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Atualizar centro de custo",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Atualizado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/{id}/{email}", method = PUT)
  ResponseEntity<Void> atualizar(
      @ApiParam(value = "Centro de Custo") @Valid @RequestBody
          final CentroCustoDataContract dataContract,
      @ApiParam(value = "Identificador do centro de custo") @PathVariable("id") final String id,
      @ApiParam(value = "Identificador do usuário") @PathVariable("email") final String email) {
    CentroCusto obj = gateway.buscarPorCodigoUsuario(id, email);
    Parsers.parse(id, obj, dataContract);
    gateway.atualizar(obj);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Ativar centro de custo",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Ativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/ativar/{id}/{email}", method = PUT)
  ResponseEntity<Void> ativar(
      @ApiParam(value = "Identificador do centro de custo") @PathVariable("id") final String id,
      @ApiParam(value = "Identificador do usuário") @PathVariable("email") final String email) {
    gateway.ativar(id, email);
    return ResponseEntity.noContent().build();
  }

  @ApiOperation(
      value = "Desativar centro de custo",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Desativado com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/desativar/{id}/{email}", method = PUT)
  ResponseEntity<Void> desativar(
      @ApiParam(value = "Identificador do centro de custo") @PathVariable("id") final String id,
      @ApiParam(value = "Identificador do usuário") @PathVariable("email") final String email) {
    gateway.desativar(id, email);
    return ResponseEntity.noContent().build();
  }
}
