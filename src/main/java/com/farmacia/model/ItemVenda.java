package com.farmacia.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Linha de uma venda: um produto, a quantidade vendida e o preco unitario
 * praticado no momento da venda.
 *
 * O nome e o preco sao copiados do {@link Medicamento} para preservar o
 * historico mesmo que o cadastro do produto mude ou seja removido depois.
 */
public class ItemVenda implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long medicamentoId;
    private final String descricao;
    private final BigDecimal precoUnitario;
    private final int quantidade;

    public ItemVenda(long medicamentoId,
                     String descricao,
                     BigDecimal precoUnitario,
                     int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero");
        }
        this.medicamentoId = medicamentoId;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    /** Subtotal da linha: preco unitario multiplicado pela quantidade. */
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public long getMedicamentoId() {
        return medicamentoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
