package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.CentroCustoRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CentroCustoGateway {

    private final String msgObjectNotFound = "Centro de custo não encontrado! Codigo: ";

    private final String msgUsuarioObjectNotFound = "O usuário informado no centro de custo não existe: ";

    private final String msgObjectDuplicated = "Centro de custo já cadastrado com o nome: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private CentroCustoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Collection<CentroCusto> buscarTodosPorUsuario() {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        return repository.findByUsuarioEmail(objSecurity.getUsername());
    }

    public Collection<CentroCusto> buscarTodosAtivosPorUsuario() {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        return repository.findByUsuarioEmail(objSecurity.getUsername())
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public CentroCusto buscarPorCodigo(final String id) {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }
        Optional<CentroCusto> obj = repository.findById(id);
        CentroCusto centroCusto = obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                CentroCusto.class.getName()));
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(centroCusto.getUsuario().getEmail())) {
            return centroCusto;
        }

        return null;
    }

    public CentroCusto inserir(final CentroCusto obj) {
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
            if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
                throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                        CentroCusto.class.getName());
            }
            if (repository.findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
                    .isPresent()) {
                throw new ObjectDuplicatedException(msgObjectDuplicated + obj.getNome() + msgTipo +
                        CentroCusto.class.getName());
            }
            obj.setId(null);
            obj.setDataInclusao(LocalDateTime.now());
            return repository.save(obj);
        }

        return null;
    }

    public CentroCusto atualizar(final CentroCusto obj) {
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
            if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
                throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                        CentroCusto.class.getName());
            }
            obj.setDataAlteracao(LocalDateTime.now());
            return repository.save(obj);
        }

        return null;
    }

    public CentroCusto ativar(final String id) {
        CentroCusto obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return repository.save(obj);
    }

    public CentroCusto desativar(final String id) {
        CentroCusto obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return repository.save(obj);
    }
}