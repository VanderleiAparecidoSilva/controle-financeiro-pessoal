package com.vanderlei.cfp.http;

import com.vanderlei.cfp.gateways.AuthorizationGateway;
import com.vanderlei.cfp.http.data.EmailDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.AUTHORIZATION)
public class AuthorizationController {

  static final String TAG_CONTROLLER = "authorization-controller";

  @Autowired private AuthorizationGateway authorizationGateway;

  @ApiOperation(
      value = "Esqueci minha senha",
      tags = {
        TAG_CONTROLLER,
      })
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Nova senha enviada com sucesso!"),
        @ApiResponse(code = 400, message = "Request inválido")
      })
  @RequestMapping(value = "/forgot", produces = MediaType.APPLICATION_JSON_VALUE, method = POST)
  ResponseEntity<Void> forgot(@Valid @RequestBody EmailDataContract dataContract) {
    authorizationGateway.enviarNovaSenha(dataContract.getEmail());
    return ResponseEntity.noContent().build();
  }
}
