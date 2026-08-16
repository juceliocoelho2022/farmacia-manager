package com.farmacia.service;

import com.farmacia.model.Medicamento;
import com.farmacia.model.ResumoDashboard;
import com.farmacia.model.Venda;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.VendaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Consolida indicadores para a tela inicial (dashboard).
 */
public class DashboardService {

    private final MedicamentoRepository medicamentoRepository;
    private final VendaRepository vendaRepository;

    public DashboardService(MedicamentoRepository medicamentoRepository,
                            VendaRepository vendaRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.vendaRepository = vendaRepository;
    }

    public ResumoDashboard gerarResumo() {
        LocalDate hoje = LocalDate.now();
        List<Medicamento> produtos = medicamentoRepository.listarTodos();

        int estoqueBaixo = 0;
        int vencendo = 0;
        int vencidos = 0;
        BigDecimal valorEstoque = BigDecimal.ZERO;

        for (Medicamento m : produtos) {
            if (m.isEstoqueBaixo()) {
                estoqueBaixo++;
            }
            if (m.isVencido(hoje)) {
                vencidos++;
            } else if (m.isProximoDoVencimento(
                    hoje, EstoqueService.DIAS_ALERTA_VENCIMENTO)) {
                vencendo++;
            }
            BigDecimal preco = m.getPrecoVenda() == null
                    ? BigDecimal.ZERO : m.getPrecoVenda();
            valorEstoque = valorEstoque.add(
                    preco.multiply(
                            BigDecimal.valueOf(m.getQuantidadeEstoque())));
        }

        List<Venda> vendasHoje = vendaRepository.listarPorData(hoje);
        BigDecimal faturamentoHoje = BigDecimal.ZERO;
        for (Venda v : vendasHoje) {
            faturamentoHoje = faturamentoHoje.add(v.getTotal());
        }

        return new ResumoDashboard(
                produtos.size(),
                estoqueBaixo,
                vencendo,
                vencidos,
                vendasHoje.size(),
                faturamentoHoje,
                valorEstoque);
    }
}
