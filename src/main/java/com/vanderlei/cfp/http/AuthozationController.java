package com.vanderlei.cfp.http;

import com.vanderlei.cfp.gateways.AuthorizationGateway;
import com.vanderlei.cfp.gateways.UsuarioSecurityGateway;
import com.vanderlei.cfp.http.data.EmailDataContract;
import com.vanderlei.cfp.http.mapping.UrlMapping;
import com.vanderlei.cfp.security.JWTUtil;
import com.vanderlei.cfp.security.UsuarioSecurity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(UrlMapping.AUTHORIZATION)
public class AuthozationController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthorizationGateway authorizationGateway;

    @ApiOperation(value = "Atualizar token de autorização")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sucesso")
    })
    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UsuarioSecurity usuarioSecurity = UsuarioSecurityGateway.authenticated();
        String token = jwtUtil.generateToken(usuarioSecurity.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Esqueci minha senha")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully")
    })
    @RequestMapping(value = "/forgot", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDataContract dataContract) {
        authorizationGateway.enviarNovaSenha(dataContract.getEmail());
        return ResponseEntity.noContent().build();
    }
}