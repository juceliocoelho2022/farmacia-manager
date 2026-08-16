package com.farmacia.repository;

import com.farmacia.model.Venda;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de persistencia para {@link Venda}.
 */
public interface VendaRepository {

    /** Insere a venda, atribuindo um id, e devolve o registro persistido. */
    Venda salvar(Venda venda);

    /** Todas as vendas, da mais recente para a mais antiga. */
    List<Venda> listarTodas();

    /** Vendas realizadas na data informada. */
    List<Venda> listarPorData(LocalDate data);
}
