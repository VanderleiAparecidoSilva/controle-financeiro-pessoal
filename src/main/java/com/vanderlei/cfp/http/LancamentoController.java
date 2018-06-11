package com.vanderlei.cfp.http;

import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.gateways.LancamentoGateway;
import com.vanderlei.cfp.gateways.converters.*;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.LANCAMENTO)
public class LancamentoController {

    @Autowired
    private LancamentoGateway gateway;

    @Autowired
    private LancamentoDataContractConverter dataContractConverter;

    @Autowired
    private LancamentoConverter converter;

    @Autowired
    private TituloLancamentoDataContractConverter tituloLancamentoDataContractConverter;

    @Autowired
    private CentroCustoDataContractConverter centroCustoDataContractConverter;

    @Autowired
    private ContaBancariaDataContractConverter contaBancariaDataContractConverter;

    @Autowired
    private UsuarioDataContractConverter usuarioDataContractConverter;

    @ApiOperation(value = "Buscar todos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Lancamento>> buscaTodos() {
        Collection<Lancamento> objList = gateway.buscarTodos();
        return ResponseEntity
                .ok().body(objList);
    }

    @ApiOperation(value = "Criar novo")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Inserido com sucesso")
    })
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> inserir(@Valid @RequestBody final LancamentoDataContract dataContract) {
        Lancamento obj = converter.convert(dataContract);
        gateway.inserir(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(
                obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Mudar tipo")
    @ApiResponses( value = {
            @ApiResponse(code = 201, message = "Tipo alterado com sucesso")
    })
    @RequestMapping(value = "/tipo/{id}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> mudarTipo(@PathVariable final String id) {
        gateway.alterarTipo(id);
        return ResponseEntity.noContent().build();
    }
}
