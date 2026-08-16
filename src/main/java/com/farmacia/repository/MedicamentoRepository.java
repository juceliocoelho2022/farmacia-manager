package com.farmacia.repository;

import com.farmacia.model.Medicamento;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia para {@link Medicamento}.
 *
 * A interface isola a logica de negocio do mecanismo de armazenamento,
 * permitindo trocar a implementacao em arquivo por uma em memoria nos testes.
 */
public interface MedicamentoRepository {

    /**
     * Insere (quando o id e zero) ou atualiza um medicamento e devolve o
     * registro persistido, ja com o id atribuido.
     */
    Medicamento salvar(Medicamento medicamento);

    Optional<Medicamento> buscarPorId(long id);

    Optional<Medicamento> buscarPorCodigoBarras(String codigoBarras);

    /** Todos os produtos, ordenados por nome. */
    List<Medicamento> listarTodos();

    /** Produtos cujo nome, fabricante ou codigo contem o termo informado. */
    List<Medicamento> buscar(String termo);

    void remover(long id);
}
