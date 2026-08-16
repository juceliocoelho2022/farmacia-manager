package com.farmacia.service;

import com.farmacia.model.ItemVenda;
import com.farmacia.model.Medicamento;
import com.farmacia.model.Venda;
import com.farmacia.repository.VendaRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Regras de negocio do PDV: registra vendas e da baixa no estoque.
 */
public class VendaService {

    private final VendaRepository vendaRepository;
    private final EstoqueService estoqueService;

    public VendaService(VendaRepository vendaRepository,
                        EstoqueService estoqueService) {
        this.vendaRepository = vendaRepository;
        this.estoqueService = estoqueService;
    }

    /**
     * Registra uma venda: valida a disponibilidade de todos os itens, da
     * baixa no estoque e persiste a venda.
     *
     * A checagem de estoque e feita para todos os itens antes de qualquer
     * baixa, de modo que uma venda invalida nao altere o estoque pela metade.
     *
     * @throws IllegalArgumentException se a venda estiver vazia ou um item
     *         referenciar um produto inexistente
     * @throws EstoqueInsuficienteException se algum item exceder o estoque
     */
    public Venda registrarVenda(Venda venda) {
        if (venda == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException(
                    "A venda precisa ter ao menos um item");
        }

        // Agrupa a quantidade necessaria por produto (um produto pode
        // aparecer em mais de uma linha da venda).
        Map<Long, Integer> necessarioPorProduto = new HashMap<>();
        for (ItemVenda item : venda.getItens()) {
            necessarioPorProduto.merge(
                    item.getMedicamentoId(),
                    item.getQuantidade(),
                    Integer::sum);
        }

        // Valida tudo antes de dar baixa.
        for (Map.Entry<Long, Integer> e : necessarioPorProduto.entrySet()) {
            Medicamento m = estoqueService.buscarPorId(e.getKey())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Produto da venda nao encontrado: id "
                                    + e.getKey()));
            if (m.getQuantidadeEstoque() < e.getValue()) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para '" + m.getNome()
                                + "'. Disponivel: " + m.getQuantidadeEstoque()
                                + ", solicitado: " + e.getValue());
            }
        }

        // Da baixa no estoque.
        for (Map.Entry<Long, Integer> e : necessarioPorProduto.entrySet()) {
            estoqueService.ajustarEstoque(e.getKey(), -e.getValue());
        }

        if (venda.getDataHora() == null) {
            venda.setDataHora(LocalDateTime.now());
        }
        return vendaRepository.salvar(venda);
    }

    public List<Venda> listarTodas() {
        return vendaRepository.listarTodas();
    }
}
