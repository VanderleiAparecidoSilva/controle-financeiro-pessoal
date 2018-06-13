package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.*;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.gateways.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class DBGateway {

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private TituloLancamentoRepository tituloLancamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void instantiateTestDatabase() {
        Usuario usuario = new Usuario(null, "Vanderlei Aparecido da Silva", "vanderlei@gmail.com");

        ContaBancaria contaBancaria = new ContaBancaria(null, "Santader", "0000000-00", 13300.00, 84.90, false, false, usuario);

        CentroCusto centroCusto = new CentroCusto(null, "Supermercado", false, true, usuario);

        TituloLancamento tituloLancamento = new TituloLancamento(null, "Compra Mensal", 10, false, true, usuario);

        Lancamento lancamento = new Lancamento(null, tituloLancamento, centroCusto, LocalDate.now(), 100.00, 1, 1, contaBancaria, "Compra do mês - Carrefour",
                Status.ABERTO, Tipo.DESPESA, usuario, null);

        usuarioRepository.save(usuario);

        contaBancariaRepository.save(contaBancaria);

        centroCustoRepository.save(centroCusto);

        tituloLancamentoRepository.save(tituloLancamento);

        lancamentoRepository.save(lancamento);
    }

    public void instantiateDevDatabase() {
        Usuario usuario01 = new Usuario(null, "Vanderlei Aparecido da Silva", "vanderlei@gmail.com");
        Usuario usuario02 = new Usuario(null, "Rita de Cássia da Silva Carminati", "ritacarminati@gmail.com");

        ContaBancaria contaBancaria01 = new ContaBancaria(null, "Santader", "0000000-00", 13300.00, 84.90, false, false, usuario01);
        ContaBancaria contaBancaria02 = new ContaBancaria(null, "Santader Rita", "1111111-99", 1000.00, 340.90, true, true, usuario02);

        CentroCusto centroCusto01 = new CentroCusto(null, "Supermercado", false, true, usuario01);
        CentroCusto centroCusto02 = new CentroCusto(null, "Estetica", false, true, usuario02);

        TituloLancamento tituloLancamento01 = new TituloLancamento(null, "Compra Mensal", 10, false, true, usuario01);
        TituloLancamento tituloLancamento02 = new TituloLancamento(null, "Tratamento com Massagem", 10, false, true, usuario02);

        Lancamento lancamento01 = new Lancamento(null, tituloLancamento01, centroCusto01, LocalDate.now(), 100.00, 1, 1, contaBancaria01, "Compra do mês - Carrefour",
                Status.ABERTO, Tipo.DESPESA, usuario01, null);
        Lancamento lancamento02 = new Lancamento(null, tituloLancamento02, centroCusto02, LocalDate.now(), 100.00, 5, 1, contaBancaria02, "Tratamento com esteticista",
                Status.ABERTO, Tipo.DESPESA, usuario02, null);
        Lancamento lancamento021 = new Lancamento(null, tituloLancamento02, centroCusto02, LocalDate.now(), 100.00, 5, 2, contaBancaria02, "Tratamento com esteticista",
                Status.ABERTO, Tipo.DESPESA, usuario02, null);
        Lancamento lancamento022 = new Lancamento(null, tituloLancamento02, centroCusto02, LocalDate.now(), 100.00, 5, 3, contaBancaria02, "Tratamento com esteticista",
                Status.ABERTO, Tipo.DESPESA, usuario02, null);
        Lancamento lancamento023 = new Lancamento(null, tituloLancamento02, centroCusto02, LocalDate.now(), 100.00, 5, 4, contaBancaria02, "Tratamento com esteticista",
                Status.ABERTO, Tipo.DESPESA, usuario02, null);
        Lancamento lancamento024 = new Lancamento(null, tituloLancamento02, centroCusto02, LocalDate.now(), 100.00, 5, 5, contaBancaria02, "Tratamento com esteticista",
                Status.ABERTO, Tipo.DESPESA, usuario02, null);

        usuarioRepository.saveAll(Arrays.asList(usuario01, usuario02));

        contaBancariaRepository.saveAll(Arrays.asList(contaBancaria01, contaBancaria02));

        centroCustoRepository.saveAll(Arrays.asList(centroCusto01, centroCusto02));

        tituloLancamentoRepository.saveAll(Arrays.asList(tituloLancamento01, tituloLancamento02));

        lancamentoRepository.saveAll(Arrays.asList(lancamento01, lancamento02, lancamento021, lancamento022, lancamento023, lancamento024));
    }
}