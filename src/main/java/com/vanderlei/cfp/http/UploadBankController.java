package com.vanderlei.cfp.http;

import com.vanderlei.cfp.gateways.UploadBankGateway;
import com.vanderlei.cfp.gateways.converters.UploadBankDataContractConverter;
import com.vanderlei.cfp.http.data.UploadBankDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.UPLOAD_BANK)
public class UploadBankController {

  static final String TAG_CONTROLLER = "uploadbank-controller";

  @Autowired private UploadBankGateway gateway;

  @Autowired private UploadBankDataContractConverter dataContractConverter;

  @ApiOperation(
      value = "Busca dados do banco para executar upload",
      response = UploadBankDataContract.class,
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Dados do banco encontrado",
            response = UploadBankDataContract.class),
        @ApiResponse(code = 400, message = "Request inválido"),
        @ApiResponse(code = 404, message = "Dados do banco não encontrado")
      })
  @RequestMapping(
      value = "/email/id",
      produces = {APPLICATION_JSON_VALUE},
      method = GET)
  ResponseEntity<UploadBankDataContract> buscaDadosBancoPorIdUsuario(
      @ApiParam(value = "Identificador do usuário", required = true) @RequestParam(value = "email")
          final String email,
      @ApiParam(value = "Identificador do banco", required = true) @RequestParam(value = "id")
          final String id) {
    UploadBankDataContract obj =
        dataContractConverter.convert(gateway.findByIdAndUsuarioEmail(id, email));
    return ResponseEntity.ok().body(obj);
  }
}
