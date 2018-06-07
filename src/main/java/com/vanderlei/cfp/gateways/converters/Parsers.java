package com.vanderlei.cfp.gateways.converters;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.http.data.CentroCustoDataContract;
import com.vanderlei.cfp.http.data.UsuarioDataContract;
import org.springframework.stereotype.Component;

@Component
public class Parsers {

    public static void parse(final String id, final CentroCusto centroCusto, final CentroCustoDataContract dataContract) {
        UsuarioConverter usuarioConverter = new UsuarioConverter();

        centroCusto.setId(id);
        centroCusto.setNome(dataContract.getNome());
        centroCusto.setAplicarNaDespesa(dataContract.getAplicarNaDespesa());
        centroCusto.setAplicarNaReceita(dataContract.getAplicarNaReceita());
        centroCusto.setUsuario(usuarioConverter.convert(dataContract.getUsuario()));
    }

    public static void parse(final String id, final Usuario usuario, final UsuarioDataContract dataContract) {
        usuario.setId(id);
        usuario.setNome(dataContract.getNome());
        usuario.setEmail(dataContract.getEmail());
    }
}
