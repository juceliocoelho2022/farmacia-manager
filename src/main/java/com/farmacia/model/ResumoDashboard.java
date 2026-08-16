package com.farmacia.model;

import java.math.BigDecimal;

/**
 * Conjunto de indicadores exibidos na tela inicial (dashboard).
 *
 * @param totalProdutos       quantidade de produtos cadastrados
 * @param produtosEstoqueBaixo produtos no estoque minimo ou abaixo dele
 * @param produtosVencendo     produtos proximos do vencimento
 * @param produtosVencidos     produtos ja vencidos
 * @param vendasHoje           numero de vendas realizadas no dia
 * @param faturamentoHoje      soma faturada no dia
 * @param valorTotalEstoque    valor de venda de todo o estoque em maos
 */
public record ResumoDashboard(
        int totalProdutos,
        int produtosEstoqueBaixo,
        int produtosVencendo,
        int produtosVencidos,
        int vendasHoje,
        BigDecimal faturamentoHoje,
        BigDecimal valorTotalEstoque) {
}
