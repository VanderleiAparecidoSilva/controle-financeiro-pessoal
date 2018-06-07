package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.gateways.CentroCustoGateway;
import com.vanderlei.cfp.gateways.converters.*;
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

    @Autowired
    private CentroCustoGateway gateway;

    @Autowired
    private CentroCustoDataContractConverter centroCustoDataContractConverter;

    @Autowired
    private CentroCustoConverter centroCustoConverter;

    @Autowired
    private UsuarioDataContractConverter usuarioDataContractConverter;

    @ApiOperation(value = "Buscar centro de custo por Codigo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> buscaPorId(@PathVariable final String id) {
        CentroCusto centroCusto = gateway.buscarPorCodigo(id);
        final CentroCustoDataContract dataContract = centroCustoDataContractConverter.convert(centroCusto);
        return ResponseEntity
                .ok().body(dataContract);
    }

    @ApiOperation(value = "Buscar todos os centros de custo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CentroCustoDataContract>> buscaTodos() {
        Collection<CentroCusto> centroCustos = gateway.buscarTodos();
        Collection<CentroCustoDataContract> dataContractList = centroCustos
                .stream()
                .map(centroCusto -> new CentroCustoDataContract(centroCusto.getId(),
                        usuarioDataContractConverter.convert(centroCusto.getUsuario()),
                        centroCusto.getNome(), centroCusto.getAplicarNaDespesa(), centroCusto.getAplicarNaReceita()))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok().body(dataContractList);
    }

    @ApiOperation(value = "Buscar todos os centros de custo ativos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/ativos", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CentroCustoDataContract>> buscaTodosAtivos() {
        Collection<CentroCusto> centroCustos = gateway.buscarTodosAtivos();
        Collection<CentroCustoDataContract> dataContractList = centroCustos
                .stream()
                .map(centroCusto -> new CentroCustoDataContract(centroCusto.getId(),
                        usuarioDataContractConverter.convert(centroCusto.getUsuario()),
                        centroCusto.getNome(), centroCusto.getAplicarNaDespesa(), centroCusto.getAplicarNaReceita()))
                .collect(Collectors.toList());
        return ResponseEntity
                .ok().body(dataContractList);
    }

    @ApiOperation(value = "Criar novo centro de custo")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Centro de custo inserido com sucesso")
    })
    @RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> inserir(@Valid @RequestBody final CentroCustoDataContract dataContract) {
        CentroCusto centroCusto = centroCustoConverter.convert(dataContract);
        gateway.inserir(centroCusto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(
                centroCusto.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Atualizar centro de custo")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Centro de custo atualizado com sucesso")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> atualizar(@Valid @RequestBody final CentroCustoDataContract dataContract,
                                          @PathVariable final String id) {
        CentroCusto centroCusto = gateway.buscarPorCodigo(id);
        Parsers.parse(id, centroCusto, dataContract);
        gateway.atualizar(centroCusto);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Ativar centro de custo")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Centro de custo ativado com sucesso")
    })
    @RequestMapping(value = "/{id}/ativar", method = RequestMethod.PUT)
    public ResponseEntity<Void> ativar(@PathVariable final String id) {
        gateway.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Desativar centro de custo")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Centro de custo desativado com sucesso")
    })
    @RequestMapping(value = "/{id}/desativar", method = RequestMethod.PUT)
    public ResponseEntity<Void> desativar(@PathVariable final String id) {
        gateway.desativar(id);
        return ResponseEntity.noContent().build();
    }
}