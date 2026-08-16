package com.farmacia.service;

import com.farmacia.model.Medicamento;
import com.farmacia.repository.MedicamentoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Regras de negocio para o cadastro e o controle de estoque de produtos.
 */
public class EstoqueService {

    /** Janela padrao, em dias, para alertar sobre produtos a vencer. */
    public static final int DIAS_ALERTA_VENCIMENTO = 30;

    private final MedicamentoRepository repository;

    public EstoqueService(MedicamentoRepository repository) {
        this.repository = repository;
    }

    /**
     * Valida e salva um produto (novo ou existente).
     *
     * @throws IllegalArgumentException se os dados obrigatorios forem invalidos
     */
    public Medicamento salvar(Medicamento medicamento) {
        if (medicamento == null) {
            throw new IllegalArgumentException("Produto nao pode ser nulo");
        }
        if (medicamento.getNome() == null
                || medicamento.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do produto");
        }
        if (medicamento.getPrecoVenda() == null
                || medicamento.getPrecoVenda().signum() < 0) {
            throw new IllegalArgumentException(
                    "Preco de venda invalido");
        }
        if (medicamento.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException(
                    "Quantidade em estoque nao pode ser negativa");
        }
        String codigo = medicamento.getCodigoBarras();
        if (codigo != null && !codigo.isBlank()) {
            Optional<Medicamento> existente =
                    repository.buscarPorCodigoBarras(codigo);
            if (existente.isPresent()
                    && existente.get().getId() != medicamento.getId()) {
                throw new IllegalArgumentException(
                        "Ja existe um produto com este codigo de barras");
            }
        }
        return repository.salvar(medicamento);
    }

    public void remover(long id) {
        repository.remover(id);
    }

    public Optional<Medicamento> buscarPorId(long id) {
        return repository.buscarPorId(id);
    }

    public List<Medicamento> listarTodos() {
        return repository.listarTodos();
    }

    public List<Medicamento> buscar(String termo) {
        return repository.buscar(termo);
    }

    /**
     * Ajusta o estoque de um produto somando {@code delta} (que pode ser
     * negativo, para retiradas).
     *
     * @throws IllegalArgumentException se o produto nao existir
     * @throws EstoqueInsuficienteException se o resultado ficar negativo
     */
    public Medicamento ajustarEstoque(long medicamentoId, int delta) {
        Medicamento m = repository.buscarPorId(medicamentoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Produto nao encontrado: id " + medicamentoId));
        int novo = m.getQuantidadeEstoque() + delta;
        if (novo < 0) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para '" + m.getNome()
                            + "'. Disponivel: " + m.getQuantidadeEstoque());
        }
        m.setQuantidadeEstoque(novo);
        return repository.salvar(m);
    }

    /** Produtos no estoque minimo ou abaixo dele. */
    public List<Medicamento> listarEstoqueBaixo() {
        return repository.listarTodos().stream()
                .filter(Medicamento::isEstoqueBaixo)
                .toList();
    }

    /** Produtos que vencem nos proximos {@value #DIAS_ALERTA_VENCIMENTO} dias. */
    public List<Medicamento> listarProximosDoVencimento() {
        LocalDate hoje = LocalDate.now();
        return repository.listarTodos().stream()
                .filter(m -> m.isProximoDoVencimento(
                        hoje, DIAS_ALERTA_VENCIMENTO))
                .toList();
    }

    /** Produtos com data de validade ja expirada. */
    public List<Medicamento> listarVencidos() {
        LocalDate hoje = LocalDate.now();
        return repository.listarTodos().stream()
                .filter(m -> m.isVencido(hoje))
                .toList();
    }
}
