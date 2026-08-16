package com.farmacia.repository.arquivo;

import com.farmacia.model.Venda;
import com.farmacia.repository.memoria.VendaRepositoryMemoria;

import java.nio.file.Path;

/**
 * Repositorio de vendas que mantem os dados em memoria e os persiste em
 * arquivo apos cada venda registrada.
 */
public class VendaRepositoryArquivo extends VendaRepositoryMemoria {

    private final ArmazenamentoArquivo<Venda> armazenamento;

    public VendaRepositoryArquivo(Path arquivo) {
        this.armazenamento = new ArmazenamentoArquivo<>(arquivo);
        dados.addAll(armazenamento.carregar());
        recalcularProximoId();
    }

    @Override
    public synchronized Venda salvar(Venda venda) {
        Venda salvo = super.salvar(venda);
        armazenamento.salvar(dados);
        return salvo;
    }
}
