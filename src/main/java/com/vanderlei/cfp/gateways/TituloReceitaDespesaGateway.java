package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.TituloReceitaDespesa;
import com.vanderlei.cfp.exceptions.ObjectDuplicatedException;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.TituloReceitaDespesaRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TituloReceitaDespesaGateway {

    private final String msgObjectNotFound = "Titulo de receita e/ou despesa não encontrado! Codigo: ";

    private final String msgUsuarioObjectNotFound = "O usuário informado no titulo da receita e/ou despesa não existe: ";

    private final String msgObjectDuplicated = "Titulo da receita e/ou despesa já cadastrado com o nome: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private TituloReceitaDespesaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Collection<TituloReceitaDespesa> buscarTodos() {
        return repository.findAll();
    }

    public Collection<TituloReceitaDespesa> buscarTodosAtivos() {
        return repository.findAll()
                .stream()
                .filter(obj -> obj.getAtivo())
                .collect(Collectors.toList());
    }

    public TituloReceitaDespesa buscarPorCodigo(final String id) {
        Optional<TituloReceitaDespesa> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                TituloReceitaDespesa.class.getName()));
    }

    public TituloReceitaDespesa inserir(final TituloReceitaDespesa obj) {
        if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    TituloReceitaDespesa.class.getName());
        }
        if (repository.findByNomeAndUsuarioEmail(obj.getNome(), obj.getUsuario().getEmail())
                .isPresent()) {
            throw new ObjectDuplicatedException(msgObjectDuplicated + obj.getNome() + msgTipo +
                    TituloReceitaDespesa.class.getName());
        }
        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public TituloReceitaDespesa atualizar(final TituloReceitaDespesa obj) {
        if (!usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    TituloReceitaDespesa.class.getName());
        }
        obj.setDataAlteracao(LocalDateTime.now());
        return repository.save(obj);
    }

    public TituloReceitaDespesa ativar(final String id) {
        TituloReceitaDespesa obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        return repository.save(obj);
    }

    public TituloReceitaDespesa desativar(final String id) {
        TituloReceitaDespesa obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        return repository.save(obj);
    }
}