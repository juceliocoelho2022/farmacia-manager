package com.farmacia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Um pedido/venda, com dados do cliente, itens, forma de pagamento,
 * desconto e taxa de entrega.
 */
public class Venda implements Serializable {

    private static final long serialVersionUID = 2L;

    private long id;
    private LocalDateTime dataHora;
    private final List<ItemVenda> itens;
    private FormaPagamento formaPagamento;
    private BigDecimal desconto;
    private BigDecimal taxaEntrega;

    private String clienteNome;
    private String clienteTelefone;
    private String clienteEndereco;

    public Venda() {
        this.dataHora = LocalDateTime.now();
        this.itens = new ArrayList<>();
        this.formaPagamento = FormaPagamento.DINHEIRO;
        this.desconto = BigDecimal.ZERO;
        this.taxaEntrega = BigDecimal.ZERO;
    }

    public void adicionarItem(ItemVenda item) {
        itens.add(item);
    }

    /** Soma dos subtotais de todos os itens, antes do desconto. */
    public BigDecimal getSubtotal() {
        BigDecimal soma = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            soma = soma.add(item.getSubtotal());
        }
        return soma;
    }

    /**
     * Valor final do pedido: subtotal menos o desconto (nunca negativo),
     * somado a taxa de entrega.
     */
    public BigDecimal getTotal() {
        BigDecimal base = getSubtotal()
                .subtract(desconto == null ? BigDecimal.ZERO : desconto)
                .max(BigDecimal.ZERO);
        BigDecimal taxa = taxaEntrega == null
                ? BigDecimal.ZERO : taxaEntrega;
        return base.add(taxa);
    }

    /** Quantidade total de unidades vendidas nesta venda. */
    public int getQuantidadeTotalItens() {
        int total = 0;
        for (ItemVenda item : itens) {
            total += item.getQuantidade();
        }
        return total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega == null ? BigDecimal.ZERO : taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public String getClienteEndereco() {
        return clienteEndereco;
    }

    public void setClienteEndereco(String clienteEndereco) {
        this.clienteEndereco = clienteEndereco;
    }
}
