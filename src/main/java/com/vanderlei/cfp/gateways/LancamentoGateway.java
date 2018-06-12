package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Baixa;
import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.converters.LancamentoConverter;
import com.vanderlei.cfp.gateways.repository.*;
import com.vanderlei.cfp.http.data.LancamentoDataContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class LancamentoGateway {

    private final String msgObjectNotFound = "Lançamento não encontrado! Codigo: ";

    private final String msgUsuarioObjectNotFound = "O usuário informado no lançamento não existe: ";

    private final String msgTituloObjectNotFound = "O título do lançamento informado não existe: ";

    private final String msgBaixaObjectNotFound = "As informações de baixa do título não foram informadas: ";

    private final String msgCentroCustoObjectNotFound = "O centro de custo informado não existe: ";

    private final String msgContaBancariaObjectNotFound = "A conta bancária informada não existe: ";

    private final String msgTipo = ", Tipo: ";

    @Autowired
    private LancamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TituloLancamentoRepository tituloLancamentoRepository;

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private LancamentoConverter lancamentoConverter;

    public Collection<Lancamento> buscarTodos() {
        return repository.findAll();
    }

    public Lancamento buscarPorCodigo(final String id) {
        Optional<Lancamento> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(msgObjectNotFound + id + msgTipo +
                Lancamento.class.getName()));
    }

    public Lancamento inserir(final Lancamento obj) {
        if (obj.getUsuario() != null && !usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    Lancamento.class.getName());
        }
        if (obj.getNome() != null && !tituloLancamentoRepository.findByNomeAndUsuarioEmail(obj.getNome().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgTituloObjectNotFound + obj.getNome().getNome() + msgTipo +
                    Lancamento.class.getName());
        }
        if (obj.getCentroCusto() != null && !centroCustoRepository.findByNomeAndUsuarioEmail(obj.getCentroCusto().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgCentroCustoObjectNotFound + obj.getCentroCusto().getNome() + msgTipo +
                    Lancamento.class.getName());
        }
        if (obj.getContaBancaria() != null && !contaBancariaRepository.findByNomeAndUsuarioEmail(obj.getContaBancaria().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgContaBancariaObjectNotFound + obj.getContaBancaria().getNome() + msgTipo +
                    Lancamento.class.getName());
        }

        obj.setId(null);
        obj.setDataInclusao(LocalDateTime.now());
        return repository.save(obj);
    }

    public void alterarTipo(final String id) {
        Lancamento obj = this.buscarPorCodigo(id);
        obj.setTipo(obj.getTipo().equals(Tipo.RECEITA) ? Tipo.DESPESA : Tipo.RECEITA);
        repository.save(obj);
    }

    public void baixar(final String id, final Baixa baixa) {
        //TODO criar o método findporcodigo para validar se o lancamento existe
        Optional<Lancamento> objOpt = repository.findById(id);
        objOpt.ifPresent(obj -> {
            obj.setBaixa(baixa);
            obj.setStatus(obj.getTipo().equals(Tipo.RECEITA) ? Status.RECEBIDO : Status.PAGO);
            repository.save(obj);
        });
    }

    public void ativar(final String id) {
        Lancamento obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(null);
        repository.save(obj);
    }

    public void desativar(final String id) {
        Lancamento obj = this.buscarPorCodigo(id);
        obj.setDataExclusao(LocalDateTime.now());
        repository.save(obj);
    }
}