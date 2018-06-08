package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.entities.ContaBancaria;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaBancariaGateway {

    @Autowired
    private ContaBancariaRepository repository;

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
        return obj.orElseThrow(() -> new ObjectNotFoundException("Centro de custo não encontrado! Codigo: " +
            id + ", Tipo: " + CentroCusto.class.getName()));
    }

    public ContaBancaria inserir(final ContaBancaria obj) {
        if (repository.findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
                .isPresent()) {
            throw new ObjectDuplicatedException("Conta bancária já cadastrada com o nome: " + obj.getNome() +
            ", Tipo: " + ContaBancaria.class.getName());
        }
        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public ContaBancaria atualizar(final ContaBancaria obj) {
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