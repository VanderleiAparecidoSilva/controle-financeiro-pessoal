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
    private UsuarioRepository usuarioRepository;

    public Collection<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public Collection<Usuario> buscarTodosAtivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public Usuario buscarPorCodigo(final String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElseThrow(() -> new ObjectNotFoundException("Usuario não encontrado! Codigo: " +
            id + ", Tipo: " + Usuario.class.getName()));
    }

    public Usuario inserir(final Usuario obj) {
        if (usuarioRepository.findByNomeAndEmail(obj.getNome(), obj.getEmail())
                .isPresent()) {
            throw new ObjectDuplicatedException("Usuario já cadastrado com o nome: " + obj.getNome() +
            ", Tipo: " + Usuario.class.getName());
        } else if (usuarioRepository.findByEmail(obj.getEmail()).size() > 0) {
            throw new ObjectDuplicatedException("Usuario já cadastrado com o email: " + obj.getEmail() +
                    ", Tipo: " + Usuario.class.getName());
        }
        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return usuarioRepository.save(obj);
    }

    public Usuario atualizar(final Usuario obj) {
        obj.setDataAlteracao(LocalDateTime.now());
        return usuarioRepository.save(obj);
    }

    public Usuario ativar(final String id) {
        Usuario obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return usuarioRepository.save(obj);
    }

    public Usuario desativar(final String id) {
        Usuario obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return usuarioRepository.save(obj);
    }
}