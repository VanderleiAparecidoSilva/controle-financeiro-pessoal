package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioGateway {

    @Autowired
    private UsuarioRepository repository;

    public Collection<Usuario> buscarTodos() {
        return repository.findAll();
    }

    public Collection<Usuario> buscarTodosAtivos() {
        return repository.findAll()
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public Usuario buscarPorCodigo(final String id) {
        Optional<Usuario> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Usuario não encontrado! Codigo: " +
            id + ", Tipo: " + Usuario.class.getName()));
    }

    public Usuario buscarPorEmail(final String email) {
        Optional<Usuario> obj = repository.findByEmail(email);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Usuario não encontrado! Email: " +
                email + ", Tipo: " + Usuario.class.getName()));
    }

    public Usuario inserir(final Usuario obj) {
        if (repository.findByNomeAndEmail(obj.getNome(), obj.getEmail())
                .isPresent()) {
            throw new ObjectDuplicatedException("Usuario já cadastrado com o nome: " + obj.getNome() +
            ", Tipo: " + Usuario.class.getName());
        } else if (repository.findByEmail(obj.getEmail()).isPresent()) {
            throw new ObjectDuplicatedException("Usuario já cadastrado com o email: " + obj.getEmail() +
                    ", Tipo: " + Usuario.class.getName());
        }
        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public Usuario atualizar(final Usuario obj) {
        obj.setDataAlteracao(LocalDateTime.now());
        return repository.save(obj);
    }

    public Usuario ativar(final String id) {
        Usuario obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return repository.save(obj);
    }

    public Usuario desativar(final String id) {
        Usuario obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return repository.save(obj);
    }
}