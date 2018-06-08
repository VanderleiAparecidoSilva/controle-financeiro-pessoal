package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.CentroCusto;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.CentroCustoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CentroCustoGateway {

    @Autowired
    private CentroCustoRepository repository;

    public Collection<CentroCusto> buscarTodos() {
        return repository.findAll();
    }

    public Collection<CentroCusto> buscarTodosAtivos() {
        return repository.findAll()
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public CentroCusto buscarPorCodigo(final String id) {
        Optional<CentroCusto> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Centro de custo não encontrado! Codigo: " +
            id + ", Tipo: " + CentroCusto.class.getName()));
    }

    public CentroCusto inserir(final CentroCusto obj) {
        if (repository.findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
                .isPresent()) {
            throw new ObjectDuplicatedException("Centro de custo já cadastrado com o nome: " + obj.getNome() +
            ", Tipo: " + CentroCusto.class.getName());
        }
        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public CentroCusto atualizar(final CentroCusto obj) {
        obj.setDataAlteracao(LocalDateTime.now());
        return repository.save(obj);
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