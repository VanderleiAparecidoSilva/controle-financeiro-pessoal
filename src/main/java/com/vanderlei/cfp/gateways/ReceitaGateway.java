package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Receita;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.ReceitaRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaGateway {

    private final String msgObjectNotFound = "Receita não encontrada! Codigo: ";

    private final String msgUsuarioObjectNotFound = "O usuário informado na receita não existe: ";

    private final String msgObjectDuplicated = "Receita já cadastrada com o nome: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private ReceitaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Collection<Receita> buscarTodos() {
        return repository.findAll();
    }

    public Collection<Receita> buscarTodosAtivos() {
        return repository.findAll()
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public Receita buscarPorCodigo(final String id) {
        Optional<Receita> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                Receita.class.getName()));
    }

    public Receita inserir(final Receita obj) {
        if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    Receita.class.getName());
        }
        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public Receita atualizar(final Receita obj) {
        if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    Receita.class.getName());
        }
        obj.setDataAlteracao(LocalDateTime.now());
        return repository.save(obj);
    }

    public Receita ativar(final String id) {
        Receita obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return repository.save(obj);
    }

    public Receita desativar(final String id) {
        Receita obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return repository.save(obj);
    }
}
