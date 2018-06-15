package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.gateways.UsuarioGateway;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.UsuarioConverter;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.USUARIO)
public class UsuarioController {

    @Autowired
    private UsuarioGateway gateway;

    @Autowired
    private UsuarioDataContractConverter dataContractConverter;

    @Autowired
    private UsuarioConverter converter;

    @ApiOperation(value = "Buscar por codigo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaPorId(@PathVariable final String id) {
        Usuario obj = gateway.buscarPorCodigo(id);
        final UsuarioDataContract dataContract = dataContractConverter.convert(obj);
        return ResponseEntity
                .ok().body(dataContract);
    }

    @ApiOperation(value = "Buscar por email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaPorEmail(@PathVariable final String email) {
        Usuario obj = gateway.buscarPorEmail(email);
        final UsuarioDataContract dataContract = dataContractConverter.convert(obj);
        return ResponseEntity
                .ok().body(dataContract);
    }

    @ApiOperation(value = "Buscar todos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UsuarioDataContract>> buscaTodos() {
        Collection<Usuario> objList = gateway.buscarTodos();
        Collection<UsuarioDataContract> dataContractList = objList
                .stream()
                .map(obj -> new UsuarioDataContract(obj.getId(), obj.getNome(), obj.getEmail(),
                        obj.getPermiteEmailLembrete()))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok().body(dataContractList);
    }

    @ApiOperation(value = "Buscar todos ativos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/ativos", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UsuarioDataContract>> buscaTodosAtivos() {
        Collection<Usuario> objList = gateway.buscarTodosAtivos();
        Collection<UsuarioDataContract> dataContractList = objList
                .stream()
                .map(obj -> new UsuarioDataContract(obj.getId(), obj.getNome(), obj.getEmail(),
                        obj.getPermiteEmailLembrete()))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok().body(dataContractList);
    }

    @ApiOperation(value = "Criar novo")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Inserido com sucesso")
    })
    @RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> inserir(@Valid @RequestBody final UsuarioDataContract dataContract) {
        Usuario obj = converter.convert(dataContract);
        gateway.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(
                obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Atualizar")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Atualizado com sucesso")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> atualizar(@Valid @RequestBody final UsuarioDataContract dataContract,
                                          @PathVariable final String id) {
        Usuario obj = gateway.buscarPorCodigo(id);
        Parsers.parse(id, obj, dataContract);
        gateway.atualizar(obj);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Ativar")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Ativado com sucesso")
    })
    @RequestMapping(value = "/ativar/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> ativar(@PathVariable final String id) {
        gateway.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Desativar")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Desativado com sucesso")
    })
    @RequestMapping(value = "/desativar/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> desativar(@PathVariable final String id) {
        gateway.desativar(id);
        return ResponseEntity.noContent().build();
    }
}