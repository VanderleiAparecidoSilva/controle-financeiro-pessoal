package com.vanderlei.cfp.job;

import com.vanderlei.cfp.email.EmailService;
import com.vanderlei.cfp.email.templates.TemplateLancamentoVencido;
import com.vanderlei.cfp.entities.Lancamento;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.entities.enums.Status;
import com.vanderlei.cfp.entities.enums.Tipo;
import com.vanderlei.cfp.gateways.LancamentoGateway;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class VencimentoJob implements Job {

    @Autowired
    private LancamentoGateway lancamentoGateway;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(final JobExecutionContext jobExecutionContext) {
        log.info("Início da execução do job: {}", VencimentoJob.class.getSimpleName());

        Collection<Lancamento> lancamentos = lancamentoGateway.buscarLancamentosVencidos(Status.ABERTO,
                LocalDate.now().plusDays(1));

        Map<Usuario, List<Lancamento>> receitasPorUsuario = lancamentos
                .stream()
                .filter(lancamento -> lancamento.getTipo().equals(Tipo.RECEITA))
                .filter(lancamento -> lancamento.getUsuario().getAtivo())
                .filter(lancamento -> lancamento.getUsuario().getPermiteEmailLembrete())
                .collect(Collectors.groupingBy(Lancamento::getUsuario));

        receitasPorUsuario.forEach((u, l) -> {
            TemplateLancamentoVencido templateLancamentoVencido = new TemplateLancamentoVencido(u, l);
            emailService.enviarEmailLancamentoVencidoHtml(templateLancamentoVencido);
        });

        Map<Usuario, List<Lancamento>> despesasPorUsuario = lancamentos
                .stream()
                .filter(lancamento -> lancamento.getTipo().equals(Tipo.DESPESA))
                .filter(lancamento -> lancamento.getUsuario().getAtivo())
                .filter(lancamento -> lancamento.getUsuario().getPermiteEmailLembrete())
                .collect(Collectors.groupingBy(Lancamento::getUsuario));

        despesasPorUsuario.forEach((u, l) -> {
            TemplateLancamentoVencido templateLancamentoVencido = new TemplateLancamentoVencido(u, l);
            emailService.enviarEmailLancamentoVencidoHtml(templateLancamentoVencido);
        });

        log.info("Término da execução do job: {}", VencimentoJob.class.getSimpleName());
    }
}