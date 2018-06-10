package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.ContaBancariaRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
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

    public Collection<ContaBancaria> buscarTodos() {
        return repository.findAll();
    }

    public Collection<ContaBancaria> buscarTodosAtivos() {
        return repository.findAll()
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public ContaBancaria buscarPorCodigo(final String id) {
        Optional<ContaBancaria> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                ContaBancaria.class.getName()));
    }

    public ContaBancaria inserir(final ContaBancaria obj) {
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

    public ContaBancaria atualizar(final ContaBancaria obj) {
        if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    ContaBancaria.class.getName());
        }
        obj.setDataAlteracao(LocalDateTime.now());
        return repository.save(obj);
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
}