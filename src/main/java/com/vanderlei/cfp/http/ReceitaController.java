package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.Receita;
import com.vanderlei.cfp.gateways.ReceitaGateway;
import com.vanderlei.cfp.gateways.converters.Parsers;
import com.vanderlei.cfp.gateways.converters.ReceitaConverter;
import com.vanderlei.cfp.gateways.converters.ReceitaDataContractConverter;
import com.vanderlei.cfp.gateways.converters.UsuarioDataContractConverter;
import com.vanderlei.cfp.http.data.ReceitaDataContract;
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
@RequestMapping(UrlMapping.RECEITA)
public class ReceitaController {

    @Autowired
    private ReceitaGateway gateway;

    @Autowired
    private ReceitaDataContractConverter dataContractConverter;

    @Autowired
    private ReceitaConverter converter;

    @Autowired
    private UsuarioDataContractConverter usuarioDataContractConverter;

    @ApiOperation(value = "Buscar por codigo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaPorId(@PathVariable final String id) {
        Receita obj = gateway.buscarPorCodigo(id);
        final ReceitaDataContract dataContract = dataContractConverter.convert(obj);
        return ResponseEntity
                .ok().body(dataContract);
    }

    @ApiOperation(value = "Buscar todos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReceitaDataContract>> buscaTodos() {
        Collection<Receita> objList = gateway.buscarTodos();
        Collection<ReceitaDataContract> dataContractList = objList
                .stream()
                .map(obj -> new ReceitaDataContract(obj.getId(), obj.getNome(), obj.getCentroCusto(),
                        obj.getVencimento(), obj.getValor(), obj.getQuantidadeParcelas(), obj.getContaBancaria(),
                        obj.getObservacao(), obj.getStatus(), obj.getTipo(), usuarioDataContractConverter.convert(obj.getUsuario())))
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
    public ResponseEntity<Collection<ReceitaDataContract>> buscaTodosAtivos() {
        Collection<Receita> objList = gateway.buscarTodosAtivos();
        Collection<ReceitaDataContract> dataContractList = objList
                .stream()
                .map(obj -> new ReceitaDataContract(obj.getId(), obj.getNome(), obj.getCentroCusto(),
                        obj.getVencimento(), obj.getValor(), obj.getQuantidadeParcelas(), obj.getContaBancaria(),
                        obj.getObservacao(), obj.getStatus(), obj.getTipo(), usuarioDataContractConverter.convert(obj.getUsuario())))
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
    public ResponseEntity<Void> inserir(@Valid @RequestBody final ReceitaDataContract dataContract) {
        Receita obj = converter.convert(dataContract);
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
    public ResponseEntity<Void> atualizar(@Valid @RequestBody final ReceitaDataContract dataContract,
                                          @PathVariable final String id) {
        Receita obj = gateway.buscarPorCodigo(id);
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