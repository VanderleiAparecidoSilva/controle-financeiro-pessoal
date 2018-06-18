package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.enums.Perfil;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class UsuarioSecurityGateway {

    public static Boolean userAuthenticatedById(final String id) {
        UsuarioSecurity authenticated = authenticated();
        if (authenticated == null || !authenticated.hasRole(Perfil.ADMIN) && !id.equals(authenticated.getId())) {
            throw new AuthorizationException("Acesso negado");
        }

        return true;
    }

    public static Boolean userAuthenticatedByEmail(final String userName) {
        UsuarioSecurity authenticated = authenticated();
        if (authenticated == null || !authenticated.hasRole(Perfil.ADMIN) && !userName.equals(authenticated.getUsername())) {
            throw new AuthorizationException("Acesso negado");
        }

        return true;
    }

    public static UsuarioSecurity authenticated() {
        try {
            return (UsuarioSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}