package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.ContaBancariaRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaBancariaGateway {

    private final String msgObjectNotFound = "Conta bancária não encontrada! Codigo: ";

    private final String msgUsuarioObjectNotFound = "O usuário informado na conta bancária não existe: ";

    private final String msgObjectDuplicated = "Conta bancária já cadastrada com o nome: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private ContaBancariaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Collection<ContaBancaria> buscarTodosPorUsuario() {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        return repository.findByUsuarioEmail(objSecurity.getUsername());
    }

    public Collection<ContaBancaria> buscarTodosAtivosPorUsuario() {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }

        return repository.findByUsuarioEmail(objSecurity.getUsername())
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public ContaBancaria buscarPorCodigo(final String id) {
        UsuarioSecurity objSecurity = UsuarioSecurityGateway.authenticated();
        if (objSecurity == null) {
            throw new AuthorizationException("Acesso negado");
        }
        Optional<ContaBancaria> obj = repository.findById(id);
        ContaBancaria contaBancaria = obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                ContaBancaria.class.getName()));
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(contaBancaria.getUsuario().getEmail())) {
            return contaBancaria;
        }

        return null;
    }

    public ContaBancaria inserir(final ContaBancaria obj) {
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
            if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
                throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                        ContaBancaria.class.getName());
            }
            if (repository.findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
                    .isPresent()) {
                throw new ObjectDuplicatedException(msgObjectDuplicated + obj.getNome() + msgTipo +
                        ContaBancaria.class.getName());
            }
            obj.setId(null);
            obj.setDataInclusao(LocalDateTime.now());
            return repository.save(obj);
        }

        return null;
    }

    public ContaBancaria atualizar(final ContaBancaria obj) {
        if (UsuarioSecurityGateway.userAuthenticatedByEmail(obj.getUsuario().getEmail())) {
            if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
                throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                        ContaBancaria.class.getName());
            }
            obj.setDataAlteracao(LocalDateTime.now());
            return repository.save(obj);
        }

        return null;
    }

    public ContaBancaria ativar(final String id) {
        ContaBancaria obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return repository.save(obj);
    }

    public ContaBancaria desativar(final String id) {
        ContaBancaria obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public void atualizarSaldo(final String id, final Double valor, final Operacao operacao) {
        ContaBancaria obj = this.buscarPorCodigo(id);
        obj.setDataAlteracao(LocalDateTime.now());
        obj.setSaldoContaBancaria(operacao.equals(Operacao.CREDITO) ? (obj.getSaldoContaBancaria() + valor) :
                (obj.getSaldoContaBancaria() - valor));
        repository.save(obj);
    }
}