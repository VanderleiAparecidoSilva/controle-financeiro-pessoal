package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Baixa;
import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.enums.Operacao;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.converters.LancamentoConverter;
import com.vanderlei.cfp.gateways.repository.*;
import com.vanderlei.cfp.http.ContaBancariaController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    private final String msgBaixaUserNotFound = "O usuário informado na baixa do lançamento não corresponde ao lançamento informado: Título: ";

    private final String msgLancamentoEmAberto = "O lançamento informado está em aberto. Não será possível estorná-lo: ";

    private final String msgLancamentoFechado = "O lançamento informado está fechado. Não será possível baixa-lo: ";

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
    private ContaBancariaController contaBancariaController;

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
        Lancamento objRet = null;

        if (obj.getUsuario() != null && !usuarioRepository.findByNomeAndEmail(obj.getUsuario().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgUsuarioObjectNotFound + obj.getUsuario() + msgTipo +
                    Lancamento.class.getName());
        }
        if (obj.getNome() != null &&
                !tituloLancamentoRepository.findByNomeAndUsuarioEmail(obj.getNome().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            if (obj.getTipo().equals(Tipo.RECEITA)) {
                obj.getNome().setAplicarNaReceita(true);
            } else if (obj.getTipo().equals(Tipo.DESPESA)){
                obj.getNome().setAplicarNaDespesa(true);
            }
            obj.getNome().setDataInclusao(LocalDateTime.now());
            obj.getNome().setDiaVencimento(obj.getVencimento().getDayOfMonth());
            tituloLancamentoRepository.save(obj.getNome());
        }
        if (obj.getCentroCusto() != null &&
                !centroCustoRepository.findByNomeAndUsuarioEmail(obj.getCentroCusto().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            if (obj.getTipo().equals(Tipo.RECEITA)) {
                obj.getCentroCusto().setAplicarNaReceita(true);
            } else if (obj.getTipo().equals(Tipo.DESPESA)) {
                obj.getCentroCusto().setAplicarNaDespesa(true);
            }
            obj.getCentroCusto().setDataInclusao(LocalDateTime.now());
            centroCustoRepository.save(obj.getCentroCusto());
        }
        if (obj.getContaBancaria() != null &&
                !contaBancariaRepository.findByNomeAndUsuarioEmail(obj.getContaBancaria().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgContaBancariaObjectNotFound + obj.getContaBancaria().getNome() + msgTipo +
                    Lancamento.class.getName());
        }

        for (int i = 0; i < obj.getQuantidadeParcelas(); i++) {
            obj.setId(null);
            obj.setDataInclusao(LocalDateTime.now());
            obj.setParcela(obj.isGerarParcelaUnica() ? 1 : i + 1);
            repository.save(obj);
            if (objRet == null) {
                objRet = obj;
            }
        }

        return objRet;
    }

    public void alterarTipo(final String id) {
        Lancamento obj = this.buscarPorCodigo(id);
        obj.setTipo(obj.getTipo().equals(Tipo.RECEITA) ? Tipo.DESPESA : Tipo.RECEITA);
        repository.save(obj);
    }

    public void baixar(final String id, final Baixa baixa) {
        Lancamento obj = this.buscarPorCodigo(id);
        if (obj.getStatus().equals(Status.PAGO)) {
            throw new ObjectNotFoundException(msgLancamentoFechado + obj.getNome().getNome() + ", status: " +
                    obj.getStatus().getDescricao() + msgTipo + Lancamento.class.getName());
        }
        if (!obj.getUsuario().getEmail().equals(baixa.getUsuario().getEmail()) ||
                !obj.getUsuario().getNome().equals(baixa.getUsuario().getNome())) {
            throw new ObjectNotFoundException(msgBaixaUserNotFound + obj.getNome().getNome() + ", usuário: " +
                    baixa.getUsuario() + msgTipo +
                    Lancamento.class.getName());
        }
        if (obj.getContaBancaria() != null &&
                !contaBancariaRepository.findByNomeAndUsuarioEmail(obj.getContaBancaria().getNome(), obj.getUsuario().getEmail()).isPresent()) {
            throw new ObjectNotFoundException(msgContaBancariaObjectNotFound + obj.getContaBancaria().getNome() + msgTipo +
                    Lancamento.class.getName());
        }
        obj.setBaixa(baixa);
        obj.setStatus(obj.getTipo().equals(Tipo.RECEITA) ? Status.RECEBIDO : Status.PAGO);
        repository.save(obj);

        if (baixa.getContaBancaria() != null) {
            contaBancariaRepository.findByNomeAndUsuarioEmail(baixa.getContaBancaria().getNome(), baixa.getUsuario().getEmail())
                    .ifPresent(contaBancaria -> {
                        if (contaBancaria.getAtualizarSaldoBancarioNaBaixaTitulo()) {
                            contaBancariaController.saldo(contaBancaria.getId(), obj.getValorParcela(), Operacao.DEBITO);
                        }
                    });
        }
    }

    public void estornar(final String id, final Baixa baixa) {
        Lancamento obj = this.buscarPorCodigo(id);
        if (obj.getStatus().equals(Status.ABERTO)) {
            throw new ObjectNotFoundException(msgLancamentoEmAberto + obj.getNome().getNome() + ", status: " +
                obj.getStatus().getDescricao() + msgTipo + Lancamento.class.getName());
        }
        if (!obj.getUsuario().getEmail().equals(baixa.getUsuario().getEmail()) ||
                !obj.getUsuario().getNome().equals(baixa.getUsuario().getNome())) {
            throw new ObjectNotFoundException(msgBaixaUserNotFound + obj.getNome().getNome() + ", usuário: " +
                    baixa.getUsuario() + msgTipo +
                    Lancamento.class.getName());
        }
        obj.setBaixa(null);
        obj.setStatus(Status.ABERTO);
        repository.save(obj);

        if (baixa.getContaBancaria() != null) {
            contaBancariaRepository.findByNomeAndUsuarioEmail(baixa.getContaBancaria().getNome(), baixa.getUsuario().getEmail())
                    .ifPresent(contaBancaria -> {
                        if (contaBancaria.getAtualizarSaldoBancarioNaBaixaTitulo()) {
                            contaBancariaController.saldo(contaBancaria.getId(), obj.getValorParcela(), Operacao.CREDITO);
                        }
                    });
        }
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

    public Collection<Lancamento> buscarLancamentosVencidos(final Status status, final LocalDate date) {
        return repository.findByStatusAndVencimentoBeforeOrderByUsuarioNome(status, date);
    }
}