package com.farmacia.repository.memoria;

import com.farmacia.model.Venda;
import com.farmacia.repository.VendaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementacao em memoria do {@link VendaRepository}.
 */
public class VendaRepositoryMemoria implements VendaRepository {

    protected final List<Venda> dados = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public synchronized Venda salvar(Venda venda) {
        if (venda.getId() == 0) {
            venda.setId(proximoId++);
        } else {
            proximoId = Math.max(proximoId, venda.getId() + 1);
        }
        dados.add(venda);
        return venda;
    }

    @Override
    public synchronized List<Venda> listarTodas() {
        List<Venda> copia = new ArrayList<>(dados);
        copia.sort(Comparator.comparing(Venda::getDataHora).reversed());
        return copia;
    }

    @Override
    public synchronized List<Venda> listarPorData(LocalDate data) {
        List<Venda> resultado = new ArrayList<>();
        for (Venda v : listarTodas()) {
            if (v.getDataHora() != null
                    && v.getDataHora().toLocalDate().equals(data)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /** Recalcula o proximo id a partir dos dados carregados do disco. */
    protected synchronized void recalcularProximoId() {
        long maior = 0;
        for (Venda v : dados) {
            maior = Math.max(maior, v.getId());
        }
        proximoId = maior + 1;
    }
}
