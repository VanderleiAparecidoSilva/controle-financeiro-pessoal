package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.entities.enums.Perfil;
import com.vanderlei.cfp.exceptions.AuthorizationException;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioGateway {

    private final String msgObjectNotFoundUsuarioCodigo = "Usuario não encontrado! Codigo: ";

    private final String msgObjectNotFoundUsuarioEmail = "Usuario não encontrado! Email: ";

    private final String msgObjectDuplicatedUsuarioNome = "Usuario já cadastrado com o nome: ";

    private final String msgObjectDuplicatedUsuarioEmail = "Usuario já cadastrado com o email: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private UsuarioRepository repository;

    public Usuario buscarPorCodigo(final String id) {
        if (UsuarioSecurityGateway.userAuthenticated(id)) {
            Optional<Usuario> obj = repository.findById(id);
            return obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFoundUsuarioCodigo + id + msgTipo +
                    Usuario.class.getName()));
        }

        return null;
    }

    public Usuario buscarPorEmail(final String email) {
        Optional<Usuario> obj = repository.findByEmail(email);
        return obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFoundUsuarioEmail + email + msgTipo +
                Usuario.class.getName()));
    }

    public Usuario inserir(final Usuario obj) {
        if (repository.findByNomeAndEmail(obj.getNome(), obj.getEmail())
                .isPresent()) {
            throw new ObjectDuplicatedException(msgObjectDuplicatedUsuarioNome + obj.getNome() + msgTipo +
                    Usuario.class.getName());
        } else if (repository.findByEmail(obj.getEmail()).isPresent()) {
            throw new ObjectDuplicatedException(msgObjectDuplicatedUsuarioEmail + obj.getEmail() + msgTipo +
                    Usuario.class.getName());
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