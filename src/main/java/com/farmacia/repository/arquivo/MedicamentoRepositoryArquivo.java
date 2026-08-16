package com.farmacia.repository.arquivo;

import com.farmacia.model.Medicamento;
import com.farmacia.repository.memoria.MedicamentoRepositoryMemoria;

import java.nio.file.Path;

/**
 * Repositorio de medicamentos que mantem os dados em memoria (herdado de
 * {@link MedicamentoRepositoryMemoria}) e os persiste em arquivo apos cada
 * alteracao.
 */
public class MedicamentoRepositoryArquivo extends MedicamentoRepositoryMemoria {

    private final ArmazenamentoArquivo<Medicamento> armazenamento;

    public MedicamentoRepositoryArquivo(Path arquivo) {
        this.armazenamento = new ArmazenamentoArquivo<>(arquivo);
        dados.addAll(armazenamento.carregar());
        recalcularProximoId();
    }

    @Override
    public synchronized Medicamento salvar(Medicamento medicamento) {
        Medicamento salvo = super.salvar(medicamento);
        persistir();
        return salvo;
    }

    @Override
    public synchronized void remover(long id) {
        super.remover(id);
        persistir();
    }

    private void persistir() {
        armazenamento.salvar(dados);
    }
}
