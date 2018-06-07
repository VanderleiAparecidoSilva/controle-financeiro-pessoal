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
@RequestMapping(UrlMapping.USUARIO)
public class UsuarioController {

    @Autowired
    private UsuarioGateway gateway;

    @Autowired
    private UsuarioDataContractConverter usuarioDataContractConverter;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @ApiOperation(value = "Buscar usuario por Codigo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaPorId(@PathVariable final String id) {
        Usuario usuario = gateway.buscarPorCodigo(id);
        final UsuarioDataContract dataContract = usuarioDataContractConverter.convert(usuario);
        return ResponseEntity
                .ok().body(dataContract);
    }

    @ApiOperation(value = "Buscar todos os usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UsuarioDataContract>> buscaTodos() {
        Collection<Usuario> usuarios = gateway.buscarTodos();
        Collection<UsuarioDataContract> dataContractList = usuarios
                .stream()
                .map(usuario -> new UsuarioDataContract(usuario.getId(), usuario.getNome(), usuario.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok().body(dataContractList);
    }

    @ApiOperation(value = "Buscar todos os usuarios ativos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/ativos", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UsuarioDataContract>> buscaTodosAtivos() {
        Collection<Usuario> usuarios = gateway.buscarTodosAtivos();
        Collection<UsuarioDataContract> dataContractList = usuarios
                .stream()
                .map(usuario -> new UsuarioDataContract(usuario.getId(), usuario.getNome(), usuario.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok().body(dataContractList);
    }

    @ApiOperation(value = "Criar novo usuario")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Usuario inserido com sucesso")
    })
    @RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> inserir(@Valid @RequestBody final UsuarioDataContract dataContract) {
        Usuario usuario = usuarioConverter.convert(dataContract);
        gateway.inserir(usuario);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(
                usuario.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Atualizar usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Usuario atualizado com sucesso")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> atualizar(@Valid @RequestBody final UsuarioDataContract dataContract,
                                          @PathVariable final String id) {
        Usuario usuario = gateway.buscarPorCodigo(id);
        Parsers.parse(id, usuario, dataContract);
        gateway.atualizar(usuario);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Ativar usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Usuario ativado com sucesso")
    })
    @RequestMapping(value = "/{id}/ativar", method = RequestMethod.PUT)
    public ResponseEntity<Void> ativar(@PathVariable final String id) {
        gateway.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Desativar usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Usuario desativado com sucesso")
    })
    @RequestMapping(value = "/{id}/desativar", method = RequestMethod.PUT)
    public ResponseEntity<Void> desativar(@PathVariable final String id) {
        gateway.desativar(id);
        return ResponseEntity.noContent().build();
    }
}