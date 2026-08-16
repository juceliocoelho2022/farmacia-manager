package com.farmacia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Produto vendido pela farmacia (medicamento ou item de conveniencia).
 *
 * A classe e mutavel porque os dados de cadastro sao editados na tela de
 * estoque, e implementa {@link Serializable} para ser persistida em arquivo.
 */
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador unico. Zero indica um registro ainda nao salvo. */
    private long id;

    private String nome;
    private String fabricante;
    private String codigoBarras;
    private Categoria categoria;

    private BigDecimal precoCusto;
    private BigDecimal precoVenda;

    private int quantidadeEstoque;
    private int estoqueMinimo;

    private LocalDate dataValidade;
    private boolean requerReceita;

    public Medicamento() {
        this.categoria = Categoria.MEDICAMENTO;
        this.precoCusto = BigDecimal.ZERO;
        this.precoVenda = BigDecimal.ZERO;
    }

    public Medicamento(String nome,
                       Categoria categoria,
                       BigDecimal precoVenda,
                       int quantidadeEstoque) {
        this();
        this.nome = nome;
        this.categoria = categoria;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    /** Indica se o estoque atual esta abaixo (ou igual) do minimo configurado. */
    public boolean isEstoqueBaixo() {
        return quantidadeEstoque <= estoqueMinimo;
    }

    /** Indica se o produto ja venceu na data informada. */
    public boolean isVencido(LocalDate referencia) {
        return dataValidade != null && dataValidade.isBefore(referencia);
    }

    /**
     * Indica se o produto vence dentro dos proximos {@code dias} dias
     * (inclusive), contando a partir da data de referencia.
     */
    public boolean isProximoDoVencimento(LocalDate referencia, int dias) {
        if (dataValidade == null) {
            return false;
        }
        LocalDate limite = referencia.plusDays(dias);
        return !dataValidade.isBefore(referencia)
                && !dataValidade.isAfter(limite);
    }

    // ---------------------------------------------------------------
    // Getters e setters
    // ---------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public boolean isRequerReceita() {
        return requerReceita;
    }

    public void setRequerReceita(boolean requerReceita) {
        this.requerReceita = requerReceita;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicamento)) {
            return false;
        }
        Medicamento that = (Medicamento) o;
        return id != 0 && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nome;
    }
}
