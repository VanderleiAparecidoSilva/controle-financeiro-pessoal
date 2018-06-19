package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.TituloLancamento;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.TituloLancamentoRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TituloLancamentoGateway {

    private final String msgObjectNotFound = "Titulo de receita e/ou despesa não encontrado! Codigo: ";

    private final String msgUsuarioObjectNotFound = "O usuário informado no titulo da receita e/ou despesa não existe: ";

    private final String msgObjectDuplicated = "Titulo da receita e/ou despesa já cadastrado com o nome: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private TituloLancamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Collection<TituloLancamento> buscarTodosPorUsuario() {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        return repository.findByUsuarioEmail(objSecurity.getUsername());
    }

    public Collection<TituloLancamento> buscarTodosAtivos() {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        return repository.findByUsuarioEmail(objSecurity.getUsername())
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public TituloLancamento buscarPorCodigo(final String id) {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }
        Optional<TituloLancamento> obj = repository.findById(id);
        TituloLancamento tituloLancamento = obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                TituloLancamento.class.getName()));
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(tituloLancamento.getUsuario().getEmail())) {
            return tituloLancamento;
        }

        return null;
    }

    public TituloLancamento inserir(final TituloLancamento obj) {
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
            if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
                throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                        TituloLancamento.class.getName());
            }
            if (repository.findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
                    .isPresent()) {
                throw new ObjectDuplicatedException(msgObjectDuplicated + obj.getNome() + msgTipo +
                        TituloLancamento.class.getName());
            }
            obj.setId(null);
            obj.setDataInclusao(LocalDateTime.now());
            return repository.save(obj);
        }

        return null;
    }

    public TituloLancamento atualizar(final TituloLancamento obj) {
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
            if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
                throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                        TituloLancamento.class.getName());
            }
            obj.setDataAlteracao(LocalDateTime.now());
            return repository.save(obj);
        }

        return null;
    }

    public TituloLancamento ativar(final String id) {
        TituloLancamento obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return repository.save(obj);
    }

    public TituloLancamento desativar(final String id) {
        TituloLancamento obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return repository.save(obj);
    }
}